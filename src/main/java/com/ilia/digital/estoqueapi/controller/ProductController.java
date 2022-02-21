package com.ilia.digital.estoqueapi.controller;

import com.ilia.digital.estoqueapi.util.PageableUtil;
import com.ilia.digital.estoqueapi.domain.Product;
import com.ilia.digital.estoqueapi.dto.CreateProductDto;
import com.ilia.digital.estoqueapi.service.product.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService   productService;

    @GetMapping
    public Page<Product> search(@RequestParam(value = "page", required = false) final Integer page,
                                @RequestParam(value = "size", required = false) final Integer size,
                                @RequestParam(value = "sort") final String sort,
                                @RequestParam(value = "orderBy") final String orderBy,
                                @RequestParam(value = "searchTerm", required = false, defaultValue = "") final String searchTerm)
                    {

        log.info("ProductController.search - start - input  [{},{},{},{}]", page, size, sort, searchTerm);

        return productService.findAByNameOrCode(PageableUtil.configuringPageable(page, size, sort,orderBy ),searchTerm);
    }


    @PostMapping
    public ResponseEntity<Product> create(@RequestBody CreateProductDto createProductDto)
    {

        log.info("ProductController.create - start - input  [{}]", createProductDto);
        Product productSaved = productService.create(createProductDto);
        log.info("ProductController.create - start - outPut  [{}]", productSaved);
        return  new ResponseEntity<>(productSaved, HttpStatus.CREATED);
    }
}


