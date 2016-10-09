sap.ui.define([
	"stickman/adminapp/controller/BaseController",
	"sap/ui/core/Fragment",
	"sap/ui/model/json/JSONModel",
	"sap/m/MessageToast"
], function (BaseController, Fragment, JSONModel, MessageToast) {
	"use strict";
	var entity = "Somemore";
	var Nav_SomeCreate = "someCreate";
	var Nav_SomeList = "someList";
	var Nav_NotFound = "notFound";

	return BaseController.extend("stickman.adminapp.controller.some.SomeCreate", {

		onInit: function () {
			var oRouter = this.getRouter();
			oRouter.getRoute("someCreate").attachMatched(this._onRouteMatched, this);
		},

		_onRouteMatched : function (oEvent) {
			var oModel = new JSONModel();
			this.getView().setModel(oModel);
			this.getView().bindElement("/");
		},
		
		onSavePress : function(oEvent){
			var oModel = this.getView().getModel();
			var oData = oModel.getData();
			var oBundle = this.getView().getModel("i18n").getResourceBundle();
			
			var msg = oBundle.getText("Label.Create.success", [oData.Name])
			var that = this;
			
			this.onJsonPost(
				entity, 
				oData,
				function(mData){
					MessageToast.show(msg);
					that.getRouter().navTo(Nav_SomeList);	
				}, 
				function(){
					this.getRouter().getTargets().display(Nav_NotFound, {
						fromTarget : Nav_SomeCreate
					});
				}
			);
		}	
	});

});