// package com.fs.commons.desktop.swing.comp;
//
// import java.util.Collection;
//
// import com.fs.commons.bean.binding.BindingComponent;
// import com.fs.commons.dao.dynamic.meta.FieldMeta;
// import com.fs.commons.dao.dynamic.trigger.FieldTrigger;
//
// public class FieldEventHandler implements FieldTrigger{
// private FieldMeta field;
//
// public FieldEventHandler(FieldMeta field){
// if(field == null){
// throw new NullPointerException("Field Meta can not be null");
// }
// this.field = field;
// }
//
// @Override
// public void fireFocusLost(BindingComponent comp ) {
// Collection<FieldTrigger> fieldTirrgers = field.getTriggers();
// for (FieldTrigger fieldTrigger : fieldTirrgers) {
// fieldTrigger.fireFocusLost(comp);
// }
// }
//
// @Override
// public void fireFocusGained(BindingComponent comp) {
// Collection<FieldTrigger> fieldTirrgers = field.getTriggers();
// for (FieldTrigger fieldTrigger : fieldTirrgers) {
// fieldTrigger.fireFocusGained(comp);
// }
// }
//
// @Override
// public void onSelected(BindingComponent comp) {
// Collection<FieldTrigger> fieldTirrgers = field.getTriggers();
// for (FieldTrigger fieldTrigger : fieldTirrgers) {
// fieldTrigger.onSelected(comp);
// }
//
// }
//
// @Override
// public void onUnSelected(BindingComponent comp) {
// Collection<FieldTrigger> fieldTirrgers = field.getTriggers();
// for (FieldTrigger fieldTrigger : fieldTirrgers) {
// fieldTrigger.onUnSelected(comp);
// }
//
// }
//
// }
