// package com.fs.license.server;
//
// import org.mortbay.jetty.Server;
// import org.mortbay.jetty.servlet.Context;
// import org.mortbay.jetty.servlet.ServletHolder;
//
// public class LicenseServer {
//
// public static void main(String[] args) throws Exception {
// // to load the license at startup
// LicenseRepository.getInstance(true);
//
// int port = 8181;
// if (args.length == 1) {
// port = Integer.parseInt(args[0]);
// }
//// Server server = new Server(port);
////
//// Context root = new Context(server, "/", Context.SESSIONS);
//// root.addServlet(new ServletHolder(new LicenseServlet()),
// "/license_manager");
//
//// server.start();
// }
// }
