sap.ui.define([
	"stickman/adminapp/controller/BaseController"
], function (BaseController) {
	"use strict";

	return BaseController.extend("stickman.adminapp.controller.Home", {

		onInit: function () {

		},
		
		onDisplayNotFound : function (oEvent) {
			// display the "notFound" target without changing the hash
			this.getRouter().getTargets().display("notFound", {
				fromTarget : "home"
			});
		},
		
		onNavToSomeList : function (oEvent){
			this.getRouter().navTo("someList");
		},
		
		onSomePress: function(){
			this.getRouter().navTo("someList");
		}

	});

});