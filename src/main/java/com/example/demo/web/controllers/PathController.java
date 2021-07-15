package com.example.demo.web.controllers;

import com.example.demo.domein.binding.FilePathBindingModel;
import com.example.demo.domein.models.RecordByLine;
import com.example.demo.domein.view.CoupleViewModel;
import com.example.demo.factories.RecordByLineFactory;
import com.example.demo.services.CoupleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/paths")
public class PathController extends BaseController {
    private final CoupleService coupleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PathController(CoupleService coupleService, ModelMapper modelMapper) {
        this.coupleService = coupleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView){
        return super.view("/paths/add-path");
    }

    @PostMapping("/add")
    public ModelAndView addPath(@ModelAttribute FilePathBindingModel model) {

        this.coupleService.readFile(model.getPath());
        return super.redirect("/paths/all");
    }

    @GetMapping("/all")
    public ModelAndView showResult(ModelAndView modelAndView) {
        CoupleViewModel coupleViewModel =
                this.modelMapper
                        .map(this.coupleService.show(), CoupleViewModel.class);
        modelAndView.addObject("couple", coupleViewModel);
        return super.view("/paths/result", modelAndView);
    }
}
