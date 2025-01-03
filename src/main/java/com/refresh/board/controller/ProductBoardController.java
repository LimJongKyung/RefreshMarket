package com.refresh.board.controller;

import com.refresh.board.service.PBoardService;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/products")
public class ProductBoardController {

    @Autowired
    private PBoardService productService;

    // 계절 제품 목록 페이지
    @GetMapping("/season")
    public String seasonProducts(Model model) {
    	List<ProductBoardVO> products = productService.seasonProducts();
    	System.out.println("Retrieved products: " + products);
    	
        model.addAttribute("products", products);
        return "refresh/mainproductboard/seasonboard";
    }
    
    // 계절 제품 목록 페이지
    @GetMapping("/local")
    public String localProducts(Model model) {
    	List<ProductBoardVO> products = productService.localProducts();
    	System.out.println("Retrieved products: " + products);

        model.addAttribute("products", products);
        return "refresh/mainproductboard/localfoodboard";
    }
    
    // 세일 제품 목록 페이지
    @GetMapping("/sale")
    public String saleProducts(Model model) {
    	List<ProductBoardVO> products = productService.saleProducts();
    	System.out.println("Retrieved products: " + products);

        model.addAttribute("products", products);
        return "refresh/mainproductboard/saleboard";
    }
    
    // 공동구매 제품 목록 페이지
    @GetMapping("/grouppurchase")
    public String groupPurchaseProducts(Model model) {
    	List<ProductBoardVO> products = productService.groupPurchaseProducts();
    	System.out.println("Retrieved products: " + products);

        model.addAttribute("products", products);
        return "refresh/mainproductboard/grouppurchaseboard";
    }
    
    // 중소기업 제품 목록 페이지
    @GetMapping("/smallbusiness")
    public String smallBusinessProducts(Model model) {
    	List<ProductBoardVO> products = productService.smallBusinessProducts();
    	System.out.println("Retrieved products: " + products);

        model.addAttribute("products", products);
        return "refresh/mainproductboard/smallbusinessesboard";
    }
    
    /*@GetMapping("/detail")
    public String getProductDetail(@RequestParam("productId") Long productId, Model model) {
        ProductBoardVO product = productService.findById(productId);
        model.addAttribute("product", product);
        return "refresh/detail";  // product-detail.html로 이동
    }*/
    
    /*@GetMapping("/detail/{productId}")
    public String getProductDetail(@PathVariable("productId") int productId, Model model) {
        // 상품 세부사항 조회
        ProductBoardVO product = productService.findById(productId);
        
        // 상품에 대한 리뷰 목록 조회
        List<PReviewVO> reviews = productService.getReviewsByProductId(productId);
        
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        
        // 모델에 상품 정보와 리뷰 정보 추가
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        
        // 세부사항 페이지로 이동
        return "refresh/detail";
    }*/
    
    @GetMapping("/detail/{productId}")
    public String getProductDetail(@PathVariable("productId") int productId, Model model, HttpSession session) {
        // 상품 세부사항 조회
        ProductBoardVO product = productService.findById(productId);
        
        // 상품에 대한 리뷰 목록 조회
        List<PReviewVO> reviews = productService.getReviewsByProductId(productId);
        
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        
        // 모델에 상품 정보와 리뷰 정보 추가
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        
        // 세션에 상품과 리뷰 정보 저장
        session.setAttribute("product", product);
        
        // 세부사항 페이지로 이동
        return "refresh/detail";        
    }
    
    @GetMapping("/otherController")
    @ResponseBody
    public ProductBoardVO getOtherController(HttpSession session) {
        // 세션에서 상품 정보 가져오기
        ProductBoardVO product = (ProductBoardVO) session.getAttribute("product");

        if (product == null) {
            throw new RuntimeException("Product not found in session");
        }

        // JSON 형식으로 반환
        return product;
    }

}
