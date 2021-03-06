package com.ilia.digital.estoqueapi.product;

import com.ilia.digital.estoqueapi.domain.Product;

import com.ilia.digital.estoqueapi.domain.ProductCategory;
import com.ilia.digital.estoqueapi.dto.product.CreateProductDto;
import com.ilia.digital.estoqueapi.dto.product.UpdateProductDto;
import com.ilia.digital.estoqueapi.exception.BadRequestException;
import com.ilia.digital.estoqueapi.repository.ProductRepository;
import com.ilia.digital.estoqueapi.service.product.impl.ProductServiceImpl;

import com.ilia.digital.estoqueapi.util.PageableUtil;
import lombok.extern.slf4j.Slf4j;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@Slf4j
class ProductServiceImplTest {
    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @Mock
    ProductRepository productRepository;
    @Mock
    ModelMapper modelMapper;

    @Test
    void findById_cannotReturnNullOrDifferentProductIfProductExists() {

        BDDMockito.when(productRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(MockUtil
                .getProductWithId()));

        Optional<Product> product = Optional.ofNullable(productServiceImpl.findById(1L));

        Assertions.assertThat(product).isNotEmpty();
        Assertions.assertThat(product.get()).isEqualTo(MockUtil
                .getProductWithId());
    }

    @Test
    void findById_throwBadRequestExceptionIfProductNotExists() {

        BDDMockito.when(productRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productServiceImpl.findById(1L)).isInstanceOf(BadRequestException.class);
    }


    @Test
    void findAByNameOrCode_CannotEmptyAndIfSearchTermDefault() {
        BDDMockito.when(productRepository.findAByNameOrCode(ArgumentMatchers.any(Pageable.class),
                ArgumentMatchers.anyString())).thenReturn(new PageImpl(List.of(MockUtil.getProductWithId())));

        Page<Product> page = productServiceImpl.findAByNameOrCode(PageableUtil.configuringPageable(null, null,
                null, null), "");

        Assertions.assertThat(page).isNotEmpty();
        Assertions.assertThat(page.toList()).isNotEmpty();
        Assertions.assertThat(page.toList().size()).isEqualTo(1);
        Assertions.assertThat(page.toList().get(0)).isEqualTo(MockUtil.getProductWithId());
    }

    @Test
    void create_cannotNullAndTheProductCannotDifferentToInitial() {

        BDDMockito.when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(MockUtil.getProductWithId());
        BDDMockito.when(modelMapper.map(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(MockUtil.getProductWithId());
        Product product = productServiceImpl.create(MockUtil.getCreateProductDto());

        Assertions.assertThat(product).isNotNull().isEqualTo(MockUtil.getProductWithId());
        Assertions.assertThat(product.getId()).isEqualTo(product.getProductStock().getId());

    }

    @Test
    void replace_cannotNullAndTheProductCannotDifferentToInitial() {
        BDDMockito.when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(MockUtil.getProductWithIdWithChanges());
        BDDMockito.when(modelMapper.map(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(MockUtil.getProductWithId());
        BDDMockito.when(productRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(MockUtil.getProductWithId()));

        Product product = productServiceImpl.replace(MockUtil.getUpdateProductDto());

        Assertions.assertThat(product).isNotNull().isEqualTo(MockUtil.getProductWithIdWithChanges());
    }


}