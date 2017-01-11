module app.models {
    "use strict";
    export class Error {
        /**
         * Unique Id to identify the type of error. For e.g. CP Program Creation, ISIN Generation, etc.
         */
        "code"?: string;

        /**
         * Source of the error identifying the layer where the error originated. For e.g. DL Integration Layer, DL (Corda/Ethereum), Reference Data Service
         */
        "source"?: SourceEnum;

        /**
         * Short message describing the error
         */
        "message"?: string;

        /**
         * Details of the error. This could also be the complete stack trace. It would be useful for debugging
         */
        "details"?: string;
    }

    export enum SourceEnum {
            DLINTEGRATIONLAYER = <any> "DL_INTEGRATION_LAYER",
            DLR3CORDA = <any> "DL_R3CORDA",
            DLETHEREUMQUORUMCAKESHOP = <any> "DL_ETHEREUM_QUORUM_CAKESHOP",
            DLETHEREUMQUORUM = <any> "DL_ETHEREUM_QUORUM",
            REFERENCEDATASERVICE = <any> "REFERENCE_DATA_SERVICE",
            UNKNOWNSOURCE = <any> "UNKNOWN_SOURCE"
        }
}