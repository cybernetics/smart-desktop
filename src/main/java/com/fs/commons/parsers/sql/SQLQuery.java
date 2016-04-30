// package com.fs.commons.parsers.sql;
//
// import org.gibello.zql.ZConstant;
// import org.gibello.zql.ZExpression;
// import org.gibello.zql.ZQuery;
//
/// **
// *
// * @author mkiswani
// *
// */
// public class SQLQuery {
// private ZQuery query = new ZQuery();
//
// public SQLQuery(){
//
// }
//
// public SQLQuery(ZQuery query){
// this.query = query;
// }
// ////////////////////////////////////////////////////////////////////
// public ZQuery getQuery() {
// return query;
// }
// ////////////////////////////////////////////////////////////////////
// public void setQuery(ZQuery query) {
// this.query = query;
// }
// ////////////////////////////////////////////////////////////////////
// public void addCondition(String key, String operator, String value){
// ZExpression where = (ZExpression) query.getWhere();
// if(where == null){
// query.addWhere(new ZExpression(key+ ""+operator , new ZConstant(value,0)));
// }else{
// where.addOperand(new ZConstant(" and "+key+operator+value,0));
// }
// }
//
// @Override
// public String toString() {
// return query.toString();
// }
// }
