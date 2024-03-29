package com.logicgate.product.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.category.model.Category;
import com.logicgate.image.model.Picture;
import com.logicgate.product.exception.ProductNotFoundException;
import com.logicgate.product.model.NewProduct;
import com.logicgate.product.model.Product;
import com.logicgate.product.model.ProductDto;
import com.logicgate.product.model.UpdateProduct;
import com.logicgate.product.service.ProductService;
import com.logicgate.seller.exception.SellerNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @PostMapping(value = {"/addProduct"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<NewProduct> addProduct(@RequestPart("product") NewProduct newProduct,
                                                 @RequestPart("images")MultipartFile[] multipartFiles) throws IOException,
                                                                                            AppUserNotFoundException {
        List<Picture> pictures=uploadPictures(multipartFiles);
        try {
            newProduct.setPictures(pictures);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        Product product=modelMapper.map(newProduct,Product.class);
        Product post=productService.addProduct(product);
        NewProduct posted=modelMapper.map(post,NewProduct.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findProduct")
    public ResponseEntity<ProductDto> getProductById(@RequestParam("id") Long id) throws ProductNotFoundException {
        Product product=productService.fetchProductById(id);
        return new ResponseEntity<>(convertProductToDto(product), OK);
    }

    @GetMapping("/findAllProductOrByTypeOrNameOrBrandOrPartNumber")
    public ResponseEntity<List<ProductDto>> getAllProductOrByTypeOrNameOrBrandOrPartNumber(
                                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                                        @RequestParam("searchKey") String searchKey){
        return new ResponseEntity<>(productService.fetchAllProductsOrByTypeOrNameOrBrandOrPartNumber(searchKey,pageNumber)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findByProductWithinPriceRange")
    public ResponseEntity<List<ProductDto>> getProductWithinPriceRange(@RequestParam("pageNumber")Integer pageNumber,
                                                                 @RequestParam("minPrice")BigDecimal minPrice,
                                                                 @RequestParam("maxPrice")BigDecimal maxPrice){
        return new ResponseEntity<>(productService.fetchAllProductsOrByPriceWithinRange(minPrice,maxPrice,pageNumber)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findProductByPriceWithinPriceLimit")
    public ResponseEntity<List<ProductDto>> getProductByPriceWithinPriceLimit(@RequestParam("pageNumber")Integer pageNumber,
                                                                              @RequestParam("price") BigDecimal price){
        return new ResponseEntity<>(productService.fetchAllProductsOrByPriceWithinLimit(price,pageNumber)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findProductByCategory")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@RequestParam("searchKey")String searchKey)
                                                                  throws ProductNotFoundException {
        return new ResponseEntity<>(productService.fetchProductByCategory(searchKey)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findProductBySeller")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductDto>> getProductBySeller(@RequestParam("pageNumber")Integer pageNumber)
            throws SellerNotFoundException, AppUserNotFoundException {
        return new ResponseEntity<>(productService.fetchProductBySeller(pageNumber)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findProductDetails")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<ProductDto>> getProductDetails(@RequestParam("isSingleProductCheckout") Boolean isSingleProductCheckout,
                                                           @RequestParam("productId") Long id) throws AppUserNotFoundException,
            ProductNotFoundException {
        return new ResponseEntity<>(productService.fetchProductDetails(isSingleProductCheckout, id)
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()),OK);
    }

    @PutMapping("/updateProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<UpdateProduct> editProduct(@RequestBody UpdateProduct updateProduct,
                                                     @RequestParam("id")Long id) throws ProductNotFoundException {
        Product product=modelMapper.map(updateProduct,Product.class);
        Product post=productService.updateProduct(product,id);
        UpdateProduct posted=modelMapper.map(post,UpdateProduct.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteProductById(@RequestParam("id")Long id) throws ProductNotFoundException {
        productService.deleteProduct(id);
        return ResponseEntity.ok().body("Product ID "+id+" has being deleted");
    }

    @DeleteMapping("/deleteProductByCode")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteProductByCode(@RequestParam("productCode") String productCode)
                                                         throws ProductNotFoundException, AppUserNotFoundException {
        productService.deleteProductByCode (productCode);
        return ResponseEntity.ok().body("Product code "+productCode+" has been deleted");
    }

    @DeleteMapping("/deleteProducts")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<?> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.ok().body("Products has been deleted");
    }

    private ProductDto convertProductToDto(Product product){
        ProductDto productDto=new ProductDto();
//        productDto.setId(product.getId());
        productDto.setProductCode(product.getProductCode());
//        productDto.setProductType(product.getProductType());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setManufacturerWebSite(product.getManufacturerWebSite());
        productDto.setPrice(product.getPrice());
        productDto.setAvailableStockQuantity(product.getAvailableStockQuantity());
        productDto.setProductAvailability(product.getProductAvailability());
        productDto.setProductCondition(product.getProductCondition());
        productDto.setUnitOfMeasure(product.getUnitOfMeasure());
        productDto.setProductSKU(product.getProductSKU());
        productDto.setBrand(product.getBrand());
        productDto.setCategories(product.getCategories().stream().map(c ->{
            Category category=new Category();
            category.setCategoryName(c.getCategoryName());
            return category;
        }).collect(Collectors.toList()));
        productDto.setProductCondition(product.getProductCondition());
        productDto.setSpecification(product.getSpecification());
        productDto.setPictures(product.getPictures());
        productDto.setSellerName(product.getSeller().getSellerName());
        return productDto;
    }



    private List<Picture> uploadPictures(MultipartFile[] files) throws IOException {
        List<Picture> pictures=new ArrayList<>();
        for (MultipartFile multipartFile:files){
            Picture picture=new Picture(multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getBytes());
            pictures.add(picture);
        }
        return pictures;
    }
}
