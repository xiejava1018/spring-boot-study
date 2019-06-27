package com.example.demo.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.data.IngredientRepository;
import com.example.demo.pojo.Ingredient;
import com.example.demo.pojo.Ingredient.Type;
import com.example.demo.pojo.Taco;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	
	@Autowired
	 public DesignTacoController(IngredientRepository ingredientRepo) {
	 this.ingredientRepo = ingredientRepo;
	 }
	
	@GetMapping
	public String showDesignForm(Model model) {
		/*
		 * List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO",
		 * "Flour Tortilla", Type.WRAP), new Ingredient("COTO", "Corn Tortilla",
		 * Type.WRAP), new Ingredient("GRBF", "Ground Beef", Type.PROTEIN), new
		 * Ingredient("CARN", "Carnitas", Type.PROTEIN), new Ingredient("TMTO",
		 * "Diced Tomatoes", Type.VEGGIES), new Ingredient("LETC", "Lettuce",
		 * Type.VEGGIES), new Ingredient("CHED", "Cheddar", Type.CHEESE), new
		 * Ingredient("JACK", "Monterrey Jack", Type.CHEESE), new Ingredient("SLSA",
		 * "Salsa", Type.SAUCE), new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
		 */
		
		List<Ingredient> ingredients = new ArrayList<>();
		 ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		 
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		model.addAttribute("design", new Taco());
		return "design";
	}
	
	@PostMapping
	public String processDesign(@Valid  @ModelAttribute("design") Taco design,Errors errors,Model model)
	{
		if(errors.hasErrors())
		{
			return "design";
		}
		log.info("Processiong design:"+design);
		return "redirect:/orders/current";
	}

	// tag::filterByType[]
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}

}
