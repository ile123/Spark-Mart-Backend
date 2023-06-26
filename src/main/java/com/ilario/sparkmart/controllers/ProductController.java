package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<Page<DisplayProductDTO>> GetAllDisplayProducts(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                                        @RequestParam(defaultValue = "name") String sortBy,
                                                                        @RequestParam(defaultValue = "asc") String sortDir,
                                                                        @RequestParam(defaultValue = "") String keyword) {
        var allProducts = productService.getAllDisplayProduct(page, pageSize, sortBy, sortDir, keyword);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> GetProduct(@PathVariable("productId") UUID productId) {
        var product = productService.getById(productId);
        if(product == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> SaveProduct(@RequestParam("image") MultipartFile image, @RequestParam("name") String name,
                                              @RequestParam("description") String description,@RequestParam("shortDescription") String shortDescription,
                                              @RequestParam("price") Double price,@RequestParam("quantity") Integer quantity,
                                              @RequestParam("brand") String brand,@RequestParam("category") String category) throws IOException {
        productService.saveProductToTheDB(image, name, description, shortDescription, price, quantity, brand, category);
        return ResponseEntity.ok("Product saved successfully");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> UpdateProduct(@PathVariable("productId") UUID productId, @RequestParam("image") MultipartFile image, @RequestParam("name") String name,
                                                @RequestParam("description") String description,@RequestParam("shortDescription") String shortDescription,
                                                @RequestParam("price") Double price,@RequestParam("quantity") Integer quantity,
                                                @RequestParam("brand") String brand,@RequestParam("category") String category) throws IOException {
        var product = productService.getById(productId);
        if(product == null) {
            return new ResponseEntity<>("ERROR: Product not found!", HttpStatus.NOT_FOUND);
        }
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String uploadDir = "src/main/resources/images/product-photos";
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        productService.update(productId, new ProductDTO(
                productId, name,
                description, shortDescription,
                "{}", price,
                newFileName, quantity,
                brand, category));
        return new ResponseEntity<>("Product successfully updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> DeleteProduct(@PathVariable("productId") UUID productId) {
        var product = productService.getProductFromDB(productId);
        if(product == null) {
            return new ResponseEntity<>("ERROR: Product not found!", HttpStatus.NOT_FOUND);
        }
        productService.delete(productId);
        return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
    }
}
