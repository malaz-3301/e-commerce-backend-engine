package com.saea.bookecommerce.service;

import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Category;
import com.saea.bookecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Programming");
    }

    @Test
    void findAllReturnsCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> categories = categoryService.findAll();

        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().getName()).isEqualTo("Programming");
    }

    @Test
    void findByIdReturnsCategoryWhenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Programming");
    }

    @Test
    void findByIdThrowsWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void createCategory() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category saved = categoryService.create(category);

        assertThat(saved.getName()).isEqualTo("Programming");
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory() {
        Category request = new Category();
        request.setName("Software Engineering");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category updated = categoryService.update(1L, request);

        assertThat(updated.getName()).isEqualTo("Software Engineering");
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
    }
}
