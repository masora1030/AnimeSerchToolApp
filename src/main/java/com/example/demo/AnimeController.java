package com.example.demo;

import java.util.Optional;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.AnimeServer;

@Controller
public class AnimeController {
	AnimeServer client = new AnimeServer();

	@RequestMapping("/")
	public String index() {
		return "/index.html";
	}

    /**
     * <p>[概 要] HTMLエスケープ処理</p>
     * @param  str 文字列
     * @return HTMLエスケープ後の文字列
     */
	public String htmlEscape(String str){
		StringBuffer result = new StringBuffer();
		for(char c : str.toCharArray()) {
			switch (c) {
			case '&' :
			case '<' :
			case '>' :
			case '"' :
			case '\'' :
			case ' ' :
				break;
			default :
				result.append(c);
				break;
			}
		}
		return result.toString();
	}
}