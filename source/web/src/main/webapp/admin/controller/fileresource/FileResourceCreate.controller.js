sap.ui.define([
	"stickman/adminapp/controller/BaseController",
	"sap/ui/core/Fragment",
	"sap/ui/model/json/JSONModel",
	"sap/m/MessageToast"
], function (BaseController, Fragment, JSONModel, MessageToast) {
	"use strict";
	var entity = "FileResources";
	var Nav_Listener = "fileresourceCreate";
	var Nav_List = "fileresourceList";
	var Nav_NotFound = "notFound";

	return BaseController.extend("stickman.adminapp.controller.fileresource.FileResourceCreate", {

		onInit: function () {
			var oRouter = this.getRouter();
			oRouter.getRoute(Nav_Listener).attachMatched(this._onRouteMatched, this);
		},

		_onRouteMatched : function (oEvent) {
			var oModel = new JSONModel();
			this.getView().setModel(oModel);
			this.getView().bindElement("/");

			try{
			this.getView().byId("fileUpload").setValue("");
			this.getView().byId("img").setVisible(false);
			}catch(e){
				
			}
			
			var cboType = new JSONModel();
			var mData = {
					"items":[
					         {
					        	 "key":"IMAGE",
					        	 "text":"IMAGE"
					         },{
					        	 "key":"HTML",
					        	 "text":"HTML"
					         }
				         ]};
			cboType.setData(mData);
			this.getView().setModel(cboType,"cboType");
		},
		
		handleUploadComplete: function(oEvent){
			var sResponse = oEvent.getParameter("responseText");
			var oAppModel = new sap.ui.model.json.JSONModel();
			var img = this.getView().byId("img");
			
			oAppModel.setJSON(sResponse);
			
			var oView = this.getView();
			var oModel = this.getView().getModel();
			//var oData = oModel.getData();
			
			oModel.getData().DocStoreId = oAppModel.getData().DocStoreId;
			oModel.getData().DocStorePreviewId = oAppModel.getData().DocStorePreviewId;
			//oView.bindElement("/");	
			
			img.setSrc("/web/fileresource?ID="+ oAppModel.getData().DocStoreId);
			img.setVisible(true);
			
		},
		
		handleSelectionChange: function(item){
			var oModel = this.getView().getModel();
			oModel.getData().Type = item.oSource.getValue();
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
					that.getRouter().navTo(Nav_List);	
				}, 
				function(){
					this.getRouter().getTargets().display(Nav_NotFound, {
						fromTarget : Nav_Listener
					});
				}
			);
		}	
	});

});