class RestoreDataModel {
    FotagModel fotagModel;
    String errorMsg;

    RestoreDataModel(FotagModel fotagModel, String errMsg) {
        this.fotagModel = fotagModel;
        this.errorMsg = errMsg;
    }

    /**
     * @return the fotagModel
     */
    public FotagModel getFotagModel() {
        return fotagModel;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMessage() {
        return errorMsg;
    }
}