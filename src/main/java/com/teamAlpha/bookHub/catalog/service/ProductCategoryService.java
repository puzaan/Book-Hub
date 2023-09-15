package com.teamAlpha.bookHub.catalog.service;

import com.teamAlpha.bookHub.catalog.controller.ProductCategoryController;
import com.teamAlpha.bookHub.catalog.controller.ProductController;
import com.teamAlpha.bookHub.catalog.entity.ProductCategory;
import com.teamAlpha.bookHub.catalog.exception.ProductCategoryNotFoundException;
import com.teamAlpha.bookHub.catalog.model.DeleteMessage;
import com.teamAlpha.bookHub.catalog.model.ProductCategoryDto;
import com.teamAlpha.bookHub.catalog.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductCategoryService {
    private final static Logger logger = LoggerFactory.getLogger(ProductCategoryService.class);

    @Autowired
    ProductCategoryRepository productCategoryRepository;
    public ProductCategoryDto createProductCategory (ProductCategory productCategory) throws Exception {

            logger.info("create new product category");
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            ProductCategory productCategory1 = productCategoryRepository.save(productCategory);
            logger.info("successfully created product category");
            BeanUtils.copyProperties(productCategory1, productCategoryDto);
            productCategoryDto.add(linkTo(methodOn(ProductCategoryController.class).GetProductCategory()).withRel("list"));
            productCategoryDto.add(linkTo(methodOn(ProductCategoryController.class).productCategoryDetails(productCategory.getCategoryId())).withSelfRel());
            return productCategoryDto;



    }

    public ProductCategoryDto productCategoryDetail(Integer productCategoryId) throws ProductCategoryNotFoundException {
        try{

            logger.info("Fetch product Details by category id");

            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            ProductCategory productCategory = productCategoryRepository.findById(productCategoryId).get();
            System.out.println(productCategory);
            BeanUtils.copyProperties(productCategory,productCategoryDto);
            productCategoryDto.add(linkTo(methodOn(ProductCategoryController.class).GetProductCategory()).withRel("list"));
            logger.info("Successfully fetch category of Id: {}", productCategoryId);
            return productCategoryDto;

        }catch (Exception e){
            logger.info("some thing wrong with category id {}", productCategoryId );
            throw new ProductCategoryNotFoundException(productCategoryId);
        }
    }

    public List<ProductCategory> getAllProductCategory (){
        logger.info("List all Product Categories");
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();

        List<ProductCategory> listCategory = productCategoryRepository.findAll();
        return listCategory;
    }

    public CollectionModel<EntityModel<ProductCategory>> all() {
        List<ProductCategory> listCategory = productCategoryRepository.findAll();
        //Type casting
        List<ProductCategoryDto> variable = (List<ProductCategoryDto>)(List<?>) listCategory;
        logger.info("Listing all Categories");
        List<EntityModel<ProductCategory>> categoryLis = listCategory.stream()
                .map(category -> EntityModel.of(category,
                        linkTo(methodOn(ProductCategoryController.class).productCategoryDetails(category.getCategoryId())).withSelfRel(),
                        linkTo(methodOn(ProductCategoryController.class).listCategory()).withRel("category_lis"))
                )
                .collect(toList());

        return CollectionModel.of(categoryLis);

    }

    public DeleteMessage deleteProductCategory(Integer productCategoryId) throws ProductCategoryNotFoundException {

        try{
            logger.info("Deleting category by id");
            DeleteMessage deleteMessage = new DeleteMessage();
            productCategoryRepository.findById(productCategoryId).get();
            productCategoryRepository.deleteById(productCategoryId);
            logger.info("Successfully delete category id: {}", productCategoryId);
            deleteMessage.setMessage("Product of id "+ productCategoryId + " is deleted from database");
            deleteMessage.add(linkTo(methodOn(ProductCategoryController.class).listCategory()).withRel("list"));
            return(deleteMessage);

        }catch (Exception e){
            logger.error("Cannot find category id {}", productCategoryId);
            throw new ProductCategoryNotFoundException(productCategoryId);
        }

    }

    public ProductCategoryDto updateProductCategoryDetails(Integer productCategoryId, ProductCategory productCategory) throws ProductCategoryNotFoundException {

        try{
            logger.info("updating category");
            ProductCategory productCategory1 = productCategoryRepository.findById(productCategoryId).get();
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();

                if(Objects.nonNull(productCategory.getCategoryName())&& !"".equalsIgnoreCase(productCategory.getCategoryName())){
                    productCategory1.setCategoryName(productCategory.getCategoryName());
                }
                if(Objects.nonNull(productCategory.getDescription())&& !"".equalsIgnoreCase(productCategory.getDescription())){
                    productCategory1.setDescription(productCategory.getDescription());
                }
                if(Objects.nonNull(productCategory.getShopId())){
                    productCategory1.setShopId(productCategory.getShopId());
                }

                ProductCategory productCategory2 = productCategoryRepository.save(productCategory1);
                logger.info("Saved category successfully");
                BeanUtils.copyProperties(productCategory2, productCategoryDto);
            productCategoryDto.add(linkTo(methodOn(ProductCategoryController.class).GetProductCategory()).withRel("list"));
            productCategoryDto.add(linkTo(methodOn(ProductCategoryController.class).productCategoryDetails(productCategory2.getCategoryId())).withSelfRel());

            return productCategoryDto;
        }catch (Exception e){
            logger.error("Category id {} not found", productCategoryId);
            throw new ProductCategoryNotFoundException(productCategoryId);

        }
    }

}
