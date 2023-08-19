package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.dto.ProductRequestDTO;
import com.ilario.sparkmart.dto.ProductStatisticsDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.services.IOrderService;
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
    private final IOrderService orderService;

    public ProductController(IProductService productService, IOrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<Page<DisplayProductDTO>> GetAllDisplayProducts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String sortDir,
                                                                         @RequestParam(defaultValue = "") String keyword) {
        var allProducts = productService.getAllDisplayProducts(page, pageSize, sortBy, sortDir, keyword);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<DisplayProductDTO>> GetAllDisplayProductsByBrand(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortDir,
                                                                                @RequestParam(defaultValue = "") String keyword,
                                                                                @PathVariable("brand") String brand) {
        var allProducts = productService.getAllDisplayProductsByBrand(page, pageSize, sortBy, sortDir, keyword, brand);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<DisplayProductDTO>> GetAllDisplayProductsByCategory(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortDir,
                                                                                @RequestParam(defaultValue = "") String keyword,
                                                                                @PathVariable("category") String category) {
        var allProducts = productService.getAllDisplayProductsByCategory(page, pageSize, sortBy, sortDir, keyword, category);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> GetProduct(@PathVariable("productId") UUID productId) {
        try {
            var product = productService.getById(productId);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product-information/{productId}")
    public ResponseEntity<ProductStatisticsDTO> GetProductStatistic(@PathVariable("productId") UUID productId) {
        try {
            var productStatistic = orderService.getProductStatistics(productId);
            return ResponseEntity.ok(productStatistic);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> SaveProduct(@ModelAttribute ProductRequestDTO productRequestDTO) throws IOException {
        productService.saveToDB(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product saved successfully");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> UpdateProduct(@PathVariable("productId") UUID productId, @RequestParam("image") MultipartFile image, @RequestParam("name") String name,
                                                @RequestParam("description") String description, @RequestParam("shortDescription") String shortDescription, @RequestParam("specifications") String specifications,
                                                @RequestParam("price") Double price, @RequestParam("quantity") Integer quantity,
                                                @RequestParam("brand") String brand, @RequestParam("category") String category) {
        try {
            String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            FileUploadUtil.saveFile("product-photos", fileName, image);

            productService.update(productId, new ProductDTO(
                    productId, name,
                    description, shortDescription,
                    specifications, price,
                    newFileName, quantity,
                    brand, category));
            return ResponseEntity.ok("Product successfully updated!");
        } catch (ProductNotFoundException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> DeleteProduct(@PathVariable("productId") UUID productId) {
        try {
            productService.delete(productId);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}