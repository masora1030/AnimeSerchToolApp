package com.example.demo;


import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.AnimeServer;

@Controller
public class AnimeController {
	AnimeServer client = new AnimeServer();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		// ランキングの出力
		SolrDocumentList result = client.findAll().getResults();
		if (CollectionUtils.isEmpty(result)) {
			model.addAttribute("emptyMessage", "検索結果はありません。");
		    }
		model.addAttribute("resultnum", result.getNumFound() + "件ヒットしました");
		model.addAttribute("result", result);
		return "index";
	}
	
	@RequestMapping(value = "/Search", method = RequestMethod.GET)
	public String Search(Model model, @RequestParam("animetitle") String animetitle, 
									 @RequestParam("startedyear") String startedyear,
									 @RequestParam("endedyear") String endedyear,
									 @RequestParam("medium") String medium) {
		// 検索結果の出力
		SolrDocumentList result = client.SearchCategory(this.solrEscape(animetitle), startedyear, endedyear, medium).getResults();
		if (CollectionUtils.isEmpty(result)) {
			model.addAttribute("resultnum", "検索結果はありません。");
	    } else {
	    	model.addAttribute("resultnum", result.getNumFound() + "件ヒットしました。");
	    }
		model.addAttribute("result", result);
		model.addAttribute("title", animetitle);
		return "Search";
	}
	
	@RequestMapping(value = "/titlesearch", method = RequestMethod.GET)
	public String Search(Model model, @RequestParam("animetitle") String animetitle) {
		// 検索結果の出力
		SolrDocumentList result = client.SearchTitle(this.solrEscape(animetitle)).getResults();
		if (CollectionUtils.isEmpty(result)) {
			model.addAttribute("resultnum", "検索結果はありません。");
	    } else {
	    	model.addAttribute("resultnum", result.getNumFound() + "件ヒットしました。");
	    }
		model.addAttribute("result", result);
		model.addAttribute("title", animetitle);
		return "Search";
	}
	
	@RequestMapping("/AgeSearch")
	public String AgeSearch() {
		return "AgeSearch";
	}
	
	@RequestMapping(value = "/agesearch", method = RequestMethod.GET)
	public String agesearch(Model model, @RequestParam("gen") String gen) {
		SolrDocumentList result = client.SearchGen(gen).getResults();
		if (CollectionUtils.isEmpty(result)) {
			model.addAttribute("resultnum", "検索結果はありません。");
	    } else {
	    	model.addAttribute("resultnum", result.getNumFound() + "件ヒットしました。");
	    }
		model.addAttribute("result", result);
		model.addAttribute("generation", gen + "年ごろに人気だったアニメはこちら");
		return "AgeSearchResult";
	}
	
	@RequestMapping(value = "/vote", method = RequestMethod.POST)
	@ResponseBody
    public String vote(@RequestBody Syain syain) {
		client.vote(syain.getId());
		return syain.getCurrentURL();
    }
	
	public String solrEscape(String str){
		StringBuffer result = new StringBuffer();
		for(char c : str.toCharArray()) {
			switch (c) {
			case '&' :
			case '<' :
			case '>' :
			case '"' :
			case '\'' :
			case ' ' :
				result.append('\\');
				break;
			default :
				break;
			}
			result.append(c);
		}
		return result.toString();
	}
}