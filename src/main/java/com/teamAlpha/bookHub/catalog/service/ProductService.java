package com.teamAlpha.bookHub.catalog.service;

import com.teamAlpha.bookHub.catalog.controller.ProductController;
import com.teamAlpha.bookHub.catalog.entity.Product;
import com.teamAlpha.bookHub.catalog.entity.ProductCategory;
import com.teamAlpha.bookHub.catalog.exception.ProductCategoryNotFoundException;
import com.teamAlpha.bookHub.catalog.exception.ProductNotFoundException;
import com.teamAlpha.bookHub.catalog.model.DeleteMessage;
import com.teamAlpha.bookHub.catalog.model.ProductDto;
import com.teamAlpha.bookHub.catalog.repository.ProductCategoryRepository;
import com.teamAlpha.bookHub.catalog.repository.ProductRepository;
import com.teamAlpha.bookHub.communication.entity.Attachment;
import com.teamAlpha.bookHub.communication.repository.AttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    public ProductDto productDetails (Integer productId)throws ProductNotFoundException {
        logger.info("Get detail of product");
        try{
            ProductDto productDto = new ProductDto();
            Product product = productRepository.findById(productId).get();
            logger.info("Product detail is show");
            BeanUtils.copyProperties(product, productDto);
            productDto.add(linkTo(methodOn(ProductController.class).getProductDetail(product.getProductId())).withSelfRel());
            productDto.add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("list"));

            return productDto;
        }catch (Exception e){
            logger.error("Product of Id {} not found", productId);
            throw new ProductNotFoundException(productId);
        }
    }
    public ProductDto createProduct (ProductDto productDto) throws ProductCategoryNotFoundException, ProductCategoryNotFoundException {
        logger.info("Creat product");
        try{
            ProductCategory productCategory = productCategoryRepository.findById(productDto.getProductCategoryId()).get();
            Attachment attachment = attachmentRepository.findById(productDto.getImageId()).get();
            System.out.println(attachment);
            System.out.println(productCategory);
            Product product = new Product();
            BeanUtils.copyProperties(productDto, product);
            product.setProductCategory(productCategory);
            product.setAttachment(attachment);
            Product product1 = productRepository.save(product);
            logger.info("Product created successfully.");
            ProductDto productDto1 = new ProductDto();
            BeanUtils.copyProperties(product1, productDto1);
            productDto1.setProductCategoryId(productDto.getProductCategoryId());
            productDto1.add(linkTo(methodOn(ProductController.class).getProductDetail(product.getProductId())).withSelfRel());
            productDto1.add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("list"));

            return productDto1;

        }catch (Exception e){
            logger.error("Product cannot be created");
            throw new ProductCategoryNotFoundException(productDto.getProductCategoryId());
        }
    }

    public DeleteMessage deleteProduct(Integer productId) throws ProductNotFoundException{
        logger.info("Trying to delete product");
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            productRepository.deleteById(productId);
            logger.info("product delete successfully.");
            deleteMessage.setMessage("Product of id "+ productId + " is deleted from database");
            deleteMessage.add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("list"));
            return deleteMessage;
        }catch (Exception e){
            logger.error("Product with Id {} not found ", productId);
            throw new ProductNotFoundException(productId);
        }
    }


    public CollectionModel<EntityModel<Product>> getAllProduct (){
        logger.info("Finding all products");
        List<EntityModel<Product>> productList = productRepository.findAll().stream()
                .map(category -> EntityModel.of(category,
                        linkTo(methodOn(ProductController.class).getProductDetail(category.getProductId())).withRel("product Details") ,
                        linkTo(methodOn(ProductController.class).getAllProduct()).withSelfRel())
                )
                .collect(toList());
        logger.info("Show all product list");
        return CollectionModel.of(productList);
    }
    public Page<Product> getAllProductPagination (Integer page, Integer size){
//        Integer pageno;
//        if (page<=0 ){
//            pageno = 0;
//
//        }else {
//            pageno = page - 1;
//        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> product = productRepository.findAllBy(pageable);
        logger.info("Show all product list");
        return product;
    }


    public ProductDto updateProduct (Integer productId, ProductDto productDto ) throws ProductNotFoundException{
        logger.info("Trying to update product");
        try{
            Product existProduct = productRepository.findById(productId).get();
            logger.info("Trying to find product detail by product id {}", productId);
            try {
                ProductCategory productCategory = productCategoryRepository.findById(productDto.getProductCategoryId()).get();
                logger.info("Trying to find product category by id {}", productDto.getProductCategoryId());
                BeanUtils.copyProperties(productDto, existProduct);
                    existProduct.setProductCategory(productCategory);
                    existProduct.setProductId(productId);
                    Product product1 = productRepository.save(existProduct);
                    logger.info("Successfully updated product details of Id: {}", productId);

                    existProduct.getProductCategory().getCategoryId();
                    ProductDto productDto1 = new ProductDto();
                    BeanUtils.copyProperties(product1, productDto1);

                    productDto1.add(linkTo(methodOn(ProductController.class).getProductDetail(product1.getProductId())).withSelfRel());
                    productDto1.add(linkTo(methodOn(ProductController.class).getAllProduct()).withRel("list"));
                    return productDto1;

            }catch (Exception e){
                logger.error("Product Category cannot found {}", productDto.getProductCategoryId());
                throw new ProductCategoryNotFoundException(productDto.getProductCategoryId());
            }

        }catch (Exception e){
            logger.error("Product of Id {} cannot be found", productId);
            throw new ProductNotFoundException(productId);
        }


    }

}
