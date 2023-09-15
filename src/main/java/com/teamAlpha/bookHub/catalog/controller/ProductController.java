package com.teamAlpha.bookHub.catalog.controller;

import com.teamAlpha.bookHub.catalog.entity.Product;
import com.teamAlpha.bookHub.catalog.model.DeleteMessage;
import com.teamAlpha.bookHub.catalog.model.ProductDto;
import com.teamAlpha.bookHub.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;


    @PostMapping(path = "/create")
    public ResponseEntity<?> createProduct(@Valid  @RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.OK)
                        .body(productService.createProduct(productDto));
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable("id") Integer productId){
        return new ResponseEntity<>(productService.productDetails(productId),HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<?>deleteProduct(@PathVariable("id") Integer productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }
//
    @GetMapping(path = "/list")
    public ResponseEntity<?> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping(path = "/lists")
    public ResponseEntity<?> getAllProductPagination(@RequestParam(defaultValue = "1", name = "page") Integer page, @RequestParam(defaultValue = "5", name = "size") Integer size){
        return new ResponseEntity<>(productService.getAllProductPagination(page, size), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<ProductDto> updateProduct (@PathVariable("id") Integer productId, @RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productId, productDto));
    }


}
