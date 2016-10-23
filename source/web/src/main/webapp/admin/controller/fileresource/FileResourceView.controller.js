sap.ui.define([
	"stickman/adminapp/controller/BaseController",
	"sap/m/Dialog",
	"sap/m/MessageToast",
	"sap/m/Button",
	"sap/m/Text",
	"sap/ui/model/json/JSONModel"
], function (BaseController, Dialog, MessageToast, Button, Text, JSONModel) {
	"use strict";
	var entity = "FileResources";
	var Nav_SomeList = "fileresourceList";
	var Nav_NotFound = "notFound";
	var Nav_Some = "fileresourceView";

	return BaseController.extend("stickman.adminapp.controller.fileresource.FileResourceView", {

		onInit: function () {
			var oRouter = this.getRouter();
			oRouter.getRoute(Nav_Some).attachMatched(this._onRouteMatched, this);
		},

		_onRouteMatched : function (oEvent) {
			var oArgs, oView;
			var oAppModel = new sap.ui.model.json.JSONModel();
			
			oArgs = oEvent.getParameter("arguments");
			var oView = this.getView();	
			var that = this;

			oView.setBusy(true);
			
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
					
			this.onJsonGet(
				entity + "("+oArgs.id+")", 
				function(mData){
					oAppModel.setData(mData);
					oView.setModel(oAppModel);
					oView.bindElement("/");	
					that._toggleButtonsAndView(false);
					oView.setBusy(false);
				}, 
				function(){
					that.getRouter().getTargets().display(Nav_NotFound);
				}
			);
		},
		
		handleUploadComplete: function(oEvent){
			var sResponse = oEvent.getParameter("responseText");
			var oAppModel = new sap.ui.model.json.JSONModel();
			var img = this.getView().byId("img");
			
			oAppModel.setJSON(sResponse);
			
			var oView = this.getView();
			var oModel = this.getView().getModel();
			
			oModel.getData().DocStoreId = oAppModel.getData().DocStoreId;
			oModel.getData().DocStorePreviewId = oAppModel.getData().DocStorePreviewId;
			
			img.setSrc("/web/fileresource?ID="+ oAppModel.getData().DocStoreId);
			img.setVisible(true);
			
		},
				
		onEditPress : function(){
			this._oDataCopy = jQuery.extend({}, this.getView().getModel().getData());
			this._toggleButtonsAndView(true);
		},
		
		onCancelPress : function () {

			//Restore the data
			var oModel = this.getView().getModel();
			var oData = oModel.getData();

			oData = this._oDataCopy;

			oModel.setData(oData);
			this._toggleButtonsAndView(false);

		},

		onSavePress : function () {
			this._doSave();
			this._toggleButtonsAndView(false);
		},
		
		onDeletePress: function(){
			var oBundle = this.getView().getModel("i18n").getResourceBundle();
			var that = this;  
			var dialog = new Dialog({
				type: 'Message',			
				afterClose: function() {
					dialog.destroy();
				}
			});
			var name = this.getView().getModel().getProperty("/Name");
			var oMessage = new Text({text: oBundle.getText("Some.delete.message", [name])});
	        var oBeginButton = new Button({
	        	text: oBundle.getText("Label.Delete"),
	        	press: function(){
	        		that._doDelete();
	        		dialog.close();
	        	}
	        });
	        var oEndButton = new Button({
	        	text: oBundle.getText("Label.Cancel"),
	        	press: function(){
	        		dialog.close();
	        	}
	        });
			
	        dialog.setTitle(oBundle.getText("Label.Confirm"));
			dialog.addContent(oMessage);
			dialog.setBeginButton(oBeginButton);
			dialog.setEndButton(oEndButton);

			dialog.open();
		},
		
		_doDelete : function(){
			var oModel = this.getView().getModel();
			var oData = oModel.getData();
			var oBundle = this.getView().getModel("i18n").getResourceBundle();
			
			var msg = oBundle.getText("Label.Delete.success", [oData.Name])
			var that = this;
			
			this.onJsonDelete(
				entity+ "("+oData.ID+")", 				
				function(mData){
					MessageToast.show(msg);		
					that.getRouter().navTo(Nav_SomeList);
				}, 
				function(){
					that.getRouter().getTargets().display(Nav_NotFound, {
						fromTarget : Nav_Some
					});
				}
			);	
		},
		
		_doSave(){
			var oModel = this.getView().getModel();
			var oData = oModel.getData();
			var oBundle = this.getView().getModel("i18n").getResourceBundle();
			
			var that = this;
			
			this.onJsonPut(
				entity+ "("+oData.ID+")", 
				oData,
				function(mData){
					MessageToast.show(oBundle.getText('Label.Save.success', [oData.Name]));
				}, 
				function(){
					MessageToast.show(oBundle.getText('Label.Save.error', [oData.Name]));
				}
			);			
		},
		
		_toggleButtonsAndView : function (bEdit) {
			var oView = this.getView();

			// Show the appropriate action buttons
			oView.byId("edit").setVisible(!bEdit);
			oView.byId("delete").setVisible(!bEdit);
			oView.byId("save").setVisible(bEdit);
			oView.byId("cancel").setVisible(bEdit);

			// Set the right form type
			this._showFormFragment(bEdit ? "FileResourceEdit" : "FileResourceDisplay");
		},
		
		_formFragments: {},
		
		_getFormFragment: function (sFragmentName) {
			var oFormFragment = this._formFragments[sFragmentName];

			if (oFormFragment) {
				return oFormFragment;
			}

			oFormFragment = sap.ui.xmlfragment(this.getView().getId(), "stickman.adminapp.view.fileresource." + sFragmentName, this);

			return this._formFragments[sFragmentName] = oFormFragment;
		},
		
		_showFormFragment : function (sFragmentName) {
			var oPage = this.getView().byId("page");

			oPage.removeAllContent();
			oPage.insertContent(this._getFormFragment(sFragmentName));
		}
	});

});
