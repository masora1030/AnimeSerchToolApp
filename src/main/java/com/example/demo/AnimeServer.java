package com.example.demo;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class AnimeServer {

	SolrClient client;
	String solrHome;
	String solrCoreName;

	AnimeServer() {
		solrHome = "http://localhost:8983/solr/";
	    solrCoreName = "core_name/";
		client = new HttpSolrClient.Builder("http://localhost:8983/solr/core_name").build();
    }

	public void start() {
		client = new HttpSolrClient.Builder("http://localhost:8983/solr/core_name").build();
    }

	public void stop() {
        try {
        	client.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

	public void commit() {
        try {
        	client.commit();
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

	public QueryResponse query(SolrQuery solrQuery) {
        try {
			return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
    }
	
	public QueryResponse SearchCategory(String animetitle, 
										String startedyear,
										String endedyear,
										String medium) {
		String q = "animetitle:" + ((animetitle == null || animetitle.isEmpty())?"*":animetitle) + " AND startedyear:[" + ((startedyear == null || startedyear.isEmpty())?"*":startedyear) 
                + " TO " + ((endedyear == null || endedyear.isEmpty())?"*":endedyear) + "]" + " AND medium:" + ((medium == null || medium.isEmpty())?"*":medium);
		SolrQuery solrQuery = new SolrQuery(q);
		solrQuery.setRows(15000);
        try {
			return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
    }
	
	public QueryResponse SearchTitle(String animetitle) {
	String q = "animetitle:" + ((animetitle == null || animetitle.isEmpty())?"*":animetitle);
		SolrQuery solrQuery = new SolrQuery(q);
		solrQuery.setRows(15000);
		try {
		    return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}
	
	public QueryResponse findAll() {
		SolrQuery solrQuery = new SolrQuery("*:*");
		solrQuery.addSort("points", SolrQuery.ORDER.desc);
        try {
			return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
    }
	
	public QueryResponse SearchGen(String gen) {
		SolrQuery solrQuery = new SolrQuery("startedyear:[" + gen + " TO " + Integer.toString(Integer.parseInt(gen)+5) + "]");
		solrQuery.addSort("points", SolrQuery.ORDER.desc);
        try {
			return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
    }

	public QueryResponse search(String query, Integer rows) {
		SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(rows);
        try {
			return client.query(solrQuery);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
    }
	
	public void vote(String id) {
        try {
        	SolrDocument doc = client.getById(id);
        	SolrInputDocument inputdoc = new SolrInputDocument();

        	  for (String name : doc.getFieldNames()) {
        		  if (name.equals("points")) {
        			  inputdoc.addField(name, Integer.parseInt(doc.getFieldValue(name).toString())+1);
        		  } else if (name.equals("_version_")) {
        			  //do nothing
        		  } else {
        			  inputdoc.addField(name, doc.getFieldValue(name));
        		  }
        	  }
        	  
        	client.deleteById(id);
        	commit();
        	client.add(inputdoc); // version conflict
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        commit();
    }

	public void save(SolrInputDocument solrInputDocument) {
        try {
        	client.add(solrInputDocument);
		} catch (SolrServerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        commit();
    }

	public void save(List<SolrInputDocument> solrInputDocumentList) {
        for(SolrInputDocument it:solrInputDocumentList) {
            try {
            	client.add(it);
			} catch (SolrServerException | IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
        }
        commit();
    }
}