sap.ui.define([
	"stickman/adminapp/controller/BaseController"
], function (BaseController) {
	"use strict";
	var entity = "Somemore";
	var Nav_SomeCreate = "someCreate";
	var Nav_SomeList = "someList";
	var Nav_NotFound = "notFound";
	var Nav_Some = "some";
	var Nav_Home = "home";

	return BaseController.extend("stickman.adminapp.controller.some.SomeList", {

		onInit: function () {
			var oRouter = this.getRouter();
			oRouter.getRoute(Nav_SomeList).attachMatched(this._onRouteMatched, this);
		},
		
		onNavBack: function(oEvent){
			this.getRouter().navTo(Nav_Home);
		},

		_onRouteMatched : function (oEvent) {
			var oArgs, oView;
			var oAppModel = new sap.ui.model.json.JSONModel();			
			var oView = this.getView();	

			oView.setBusy(true);
			this.onJsonGet(
				entity, 
				function(mData){
					oAppModel.setData(mData);
					oView.setModel(oAppModel);
					oView.setBusy(false);
				}, 
				function(){
					router.getTargets().display(Nav_NotFound);
				}
			);
			
		},
				
		onListItemPressed : function(oEvent){
			var oItem, oCtx;

			oItem = oEvent.getSource();
			oCtx = oItem.getBindingContext();

			this.getRouter().navTo(Nav_Some,{
				id : oCtx.getProperty("ID")
			});

		},
		
		onSomeCreate: function(oEvent){
			this.getRouter().navTo(Nav_SomeCreate);
		}

	});

});