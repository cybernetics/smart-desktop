//package com.fs.commons.parsers.sql;
//
//import java.io.ByteArrayInputStream;
//
//import org.gibello.zql.ParseException;
//import org.gibello.zql.ZQuery;
//import org.gibello.zql.ZStatement;
//import org.gibello.zql.ZqlParser;
//
//import com.fs.commons.parsers.Parser;
//
///**
// * 
// * @author mkiswani
// *
// */
//public class SqlParser implements Parser{
//	private ZqlParser parser = new ZqlParser();
//	private String sql;
//
//	public SqlParser(String sql){
//		if(sql.endsWith(";")){
//			this.sql = sql;
//		}else{
//			this.sql = sql+";";
//		}
//	}
//	
//	@Override
//	public SQLQuery parse() {
//		parser.initParser(new ByteArrayInputStream(sql.getBytes()));
//		try {
//			 ZQuery query = (ZQuery) parser.readStatement();
//			return new SQLQuery(query);
//		} catch (ParseException e) {
//			throw new com.fs.commons.parsers.ParseException(e);
//		}
//	}
//	
//	
//}
