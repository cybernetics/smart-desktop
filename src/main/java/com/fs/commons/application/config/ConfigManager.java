// package com.fs.commons.application.config;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.Properties;
//
// import com.fs.commons.application.util.ResourceLoaderFactory;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.logging.Logger;
// import com.fs.commons.util.ExceptionUtil;
// import com.fs.commons.util.GeneralUtility;
//
// public class ConfigManager extends CommonsConfigManager {
// private static final String FILE_NAME = System.getProperty("db.config",
// "config.properties");
// public static final String CONFIG_FILE_NAMES[] = { FILE_NAME,
// "system.config", "system.config.xml" };
//
// /**
// * @throws FileNotFoundException
// * @throws IOException
// */
// public ConfigManager() {
// try {
// for (String name : CONFIG_FILE_NAMES) {
// InputStream in = null;
// try {
// in = GeneralUtility.getFileInputStream(name);
// load(in);
// return;
// } catch (FileNotFoundException e) {
// }
// }
// String errorMessage = "No Configuration Available.\n The System will Exit";
// errorMessage += "\nFile is not found on " + new File("X").getAbsolutePath();
// SwingUtility.showUserErrorDialog(errorMessage);
// System.exit(0);
// } catch (Exception e) {
// ExceptionUtil.handle(e);
// }
// }
//
// public ConfigManager(InputStream configStream) throws IOException {
// load(configStream);
// }
//
// /**
// * @param file
// */
// private void loadFile(File file) {
// try {
// load(file.getAbsolutePath());
// System.getProperties().putAll(getProperties());
// } catch (IOException e) {
// String errorMessage = "Error Loading Configuration File.\n The System will
// Exit\n" + file.getAbsolutePath();
// SwingUtility.showErrorDialog(errorMessage, e);
// System.exit(0);
// }
// }
// }
