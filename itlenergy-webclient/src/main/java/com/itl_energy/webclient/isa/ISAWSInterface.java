package com.itl_energy.webclient.isa;

import com.google.gson.JsonObject;
import com.itl_energy.webclient.itl.util.ApiClient;
import com.itl_energy.webclient.itl.util.ApiException;
import com.itl_energy.webclient.itl.util.ApiResponse;

/**
 * Programmatical interface to ISA's iEnergy3 Web API.
 *
 * @author Bruce Stephen
 * @date 23rd July 2013
 */
public class ISAWSInterface {

    protected String token;//provided at the point of authentication...
    protected int duration;
    protected int tokenTimestampIssue;

    private static final String urlbase = "http://api.ienergy3.isa.pt:6969";

    public ISAWSInterface() {
        this.duration = 0;
        this.tokenTimestampIssue = 0;
        this.token = null;
    }

    public boolean beginSession(String user, String password) throws ApiException {
        ApiClient client = new ApiClient(urlbase + "/api/1.0/sessions");

        JsonObject login = new JsonObject();
        login.addProperty("Login", user);
        login.addProperty("Password", password);

        ApiResponse response = client.post(login);

        if (response.success()) {
            JsonObject result = response.deserialise();
            this.token = "ISA " + result.getAsJsonPrimitive("Token").getAsString();
            this.duration = result.getAsJsonPrimitive("Timeout").getAsInt();
            return true;
        }
        else {
            return false;
        }
    }

//    public void listDevicesForHub() throws ApiException {
//        ApiClient client = new ApiClient(urlbase + "/api/1.0/units");
//        client.header("Authorization", token);
//
//        ApiResponse response = client.get();
//        //TODO...
//    }
//
//    public List<ISAHubTag> getTagsForHub(int unitid) {
//        String urlbase = "http://api.ienergy3.isa.pt:6969";
//        String path = "/api/1.0/tags";
//        String params = "?where=UnitId=" + unitid;
//        String[][] args = {{"Accept", "application/json"}, {"Authorization", "ISA " + this.token}};
//
//        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
//
//        return null;
//    }
//
//    public List<ISADeviceTag> getTagsForDevicesOnHub(int hubid) {
//        String urlbase = "http://api.ienergy3.isa.pt:6969";
//        String path = "/api/1.0/devices";
//        String params = "?where=UnitId=" + hubid;
//        String[][] args = {{"Accept", "application/json"}, {"Authorization", "ISA " + this.token}};
//
//        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
//
//        return null;
//    }
//
//    public void getEnergyConsumptionBetweenTimes(String begin, String end, String tags) {
//        String urlbase = "http://api.ienergy3.isa.pt:6969";
//        String path = "/api/1.0/consumptions/hourly";
//        String[][] args = {{"Accept", "application/json"}, {"Authorization", "ISA " + this.token}};
//
//        long time1 = ITLClientUtilities.timeToUTC(begin);
//        long time2 = ITLClientUtilities.timeToUTC(end);
//
//        String params = "?to=" + Long.toString(time2) + "&from=" + Long.toString(time1) + "&tags=[" + tags + "]";
//
//        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path + params, args);
//
//        System.out.println(response);
//    }
//
//    public List<ISATagType> getTagTypes() {
//
//        return null;
//    }
//
//    public boolean actuatePlug(int id) {
//        String urlbase = "http://api.ienergy3.isa.pt:6969";
//        String path = "/api/1.0/actuations";
//        String[][] args = {{"Accept", "application/json"}, {"Authorization", "ISA " + this.token}};
//        String content = "{\"TagId\":\"" + id + "\",\"command\":\"ACTION_ON\"}";
//
//        String response = ITLClientUtilities.makeHTTPPostRequest(urlbase + path, args, content).getContent();
//
//        System.out.println(response);
//
//        return false;
//    }
//
//    public void checkPlugState(int id) {
//        String urlbase = "http://api.ienergy3.isa.pt:6969";
//        String path = "/api/1.0/actuatorstate/" + id;
//        String[][] args = {{"Accept", "application/json"}, {"Authorization", "ISA " + this.token}};
//
//        ITLHTTPResponse response = ITLClientUtilities.makeHTTPGetRequest(urlbase + path, args);
//
//        System.out.println(response);
//    }
//
//    protected abstract class ISAWSObject {
//
//        protected JSONObject object;
//
//        protected ISAWSObject() {
//            this.object = new JSONObject();
//        }
//
//        public String toJSON() {
//            StringWriter out = new StringWriter();
//
//            try {
//                this.object.writeJSONString(out);
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//
//                return null;
//            }
//
//            return out.toString();
//        }
//
//        public abstract boolean fromJSON(String json);
//    }
//
//    protected class ISATagType extends ISAWSObject {
//
//        protected int id;
//        protected String description;
//        protected boolean accumulate;
//        protected boolean actuator;
//        protected boolean partial;
//        protected boolean visible;
//        protected int measurementUnit;
//
//        public ISATagType() {
//            this.id = -1;
//            this.description = "";
//            this.accumulate = false;
//            this.actuator = false;
//            this.partial = false;
//            this.visible = false;
//            this.measurementUnit = -1;
//        }
//
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("Id", this.id);
//            this.object.put("Description", this.description);
//            this.object.put("Accumulate", this.accumulate);
//            this.object.put("Actuator", this.actuator);
//            this.object.put("Partial", this.partial);
//            this.object.put("Visible", this.visible);
//            this.object.put("MeasurementUnit", this.measurementUnit);
//
//            return super.toJSON();
//        }
//
//        public boolean fromJSON(String json) {
//            Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);
//
//            this.id = (Integer) this.object.get("Id");
//            this.description = (String) this.object.get("Description");
//            this.accumulate = (Boolean) this.object.get("Accumulate");
//            this.actuator = (Boolean) this.object.get("Actuator");
//            this.partial = (Boolean) this.object.get("Partial");
//            this.visible = (Boolean) this.object.get("Visible");
//            this.measurementUnit = (Integer) this.object.get("MeasurementUnit");
//
//            return true;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public boolean isAccumulate() {
//            return accumulate;
//        }
//
//        public void setAccumulate(boolean accumulate) {
//            this.accumulate = accumulate;
//        }
//
//        public boolean isActuator() {
//            return actuator;
//        }
//
//        public void setActuator(boolean actuator) {
//            this.actuator = actuator;
//        }
//
//        public boolean isPartial() {
//            return partial;
//        }
//
//        public void setPartial(boolean partial) {
//            this.partial = partial;
//        }
//
//        public boolean isVisible() {
//            return visible;
//        }
//
//        public void setVisible(boolean visible) {
//            this.visible = visible;
//        }
//
//        public int getMeasurementUnit() {
//            return measurementUnit;
//        }
//
//        public void setMeasurementUnit(int measurementUnit) {
//            this.measurementUnit = measurementUnit;
//        }
//    }
//
//    protected class ISAActuator extends ISAWSObject {
//
//        protected int id;
//        protected String command;
//
//        public ISAActuator() {
//            super();
//        }
//
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("Id", this.id);
//            this.object.put("Command", this.command);
//
//            return super.toJSON();
//        }
//
//        public boolean fromJSON(String json) {
//            Map<?, ?> map = ITLClientUtilities.parseJSONResponseToMap(json);
//
//            this.id = (Integer) this.object.get("Id");
//            this.command = (String) this.object.get("Command");
//
//            return true;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//    }
//
//    protected class ISAHub extends ISAWSObject {
//
//        protected String b64access;
//        protected boolean deleted;
//        protected String[] devices;
//        protected int fwVersion;
//        protected String hashedSerial;
//        protected int hwVersion;
//        protected int id;
//        protected int interval;
//        protected boolean isActive;
//        protected boolean isComm;
//        protected String kitReference;
//        protected String lastComm;
//        protected long lastConfigDate;
//        protected String local;
//        protected String localId;
//        protected String macAddress;
//        protected String[] moduleTypes;
//        protected int monthlyObjective;
//        protected String name;
//        protected String[] notes;
//        protected String reference;
//        protected int stateid;
//        protected String timezone;
//        protected int typeid;
//
//        /*
//         {
//         "List": [
//         {
//         "B64AccessKey": "U2hhcmVkIGtleSB3aXRoIGlFbmVyZ3kz",
//         "Deleted": false,
//         "Devices": [],
//         "FwVersion": 0,
//         "HashedSerial": "7777-1185-8910-5868-9643",
//         "HwVersion": 0,
//         "Id": 2890,
//         "Interval": 15,
//         "IsActive": true,
//         "IsComm": true,
//         "KitReference": null,
//         "LastComm": null,
//         "LastConfigDate": 1373629320457,
//         "Local": null,
//         "LocalId": null,
//         "MacAddress": "00-04-A3-54-AB-2B",
//         "ModuleTypes": [],
//         "MonthlyObjective": 1000,
//         "Name": "02110906278",
//         "Notes": [],
//         "Reference": "02110906278",
//         "StateId": 1,
//         "TimeZoneId": "GMT Standard Time",
//         "TypeId": 3
//         }
//         ],
//         "Page": 0,
//         "PageSize": 50,
//         "Total": 1
//         }
//         */
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("Id", this.id);
//            this.object.put("B64AccessKey", this.b64access);
//            this.object.put("Deleted", this.deleted);
//            this.object.put("Devices", this.devices);
//            this.object.put("FwVersion", this.fwVersion);
//            this.object.put("HashedSerial", this.hashedSerial);
//            this.object.put("HwVersion", this.hwVersion);
//            this.object.put("Id", this.id);
//            this.object.put("Interval", this.interval);
//            this.object.put("IsActive", this.isActive);
//            this.object.put("IsComm", this.isComm);
//            this.object.put("KitReference", this.kitReference);
//            this.object.put("LastComm", this.lastComm);
//            this.object.put("LastConfigDate", this.lastConfigDate);
//            this.object.put("Local", this.local);
//            this.object.put("LocalId", this.localId);
//            this.object.put("MacAddress", this.macAddress);
//            this.object.put("ModuleTypes", this.moduleTypes);
//            this.object.put("MonthlyObjective", this.monthlyObjective);
//            this.object.put("Name", this.name);
//            this.object.put("Notes", this.notes);
//            this.object.put("Reference", this.reference);
//            this.object.put("StateId", this.stateid);
//            this.object.put("TimeZoneId", this.timezone);
//            this.object.put("TypeId", this.typeid);
//
//            return super.toJSON();
//        }
//
//        @Override
//        public boolean fromJSON(String json) {
//            Map<?, ?> map = (Map<?, ?>) ITLClientUtilities.parseJSONResponseToMap(json).get("List");
//
//            this.b64access = (String) map.get("B64AccessKey");
//            this.deleted = (Boolean) map.get("Deleted");
//            this.devices = (String[]) map.get("Devices");
//            this.fwVersion = (Integer) map.get("FwVersion");
//            this.hashedSerial = (String) map.get("HashedSerial");
//            this.hwVersion = (Integer) map.get("HwVersion");
//            this.id = (Integer) map.get("Id");
//            this.interval = (Integer) map.get("Interval");
//            this.isActive = (Boolean) map.get("IsActive");
//            this.isComm = (Boolean) map.get("IsComm");
//            this.kitReference = (String) map.get("KitReference");
//            this.lastComm = (String) map.get("LastComm");
//            this.lastConfigDate = (Long) map.get("LastConfigDate");
//            this.local = (String) map.get("Local");
//            this.localId = (String) map.get("LocalId");
//            this.macAddress = (String) map.get("MacAddress");
//            this.moduleTypes = (String[]) map.get("ModuleTypes");
//            this.monthlyObjective = (Integer) map.get("MonthlyObjective");
//            this.name = (String) map.get("Name");
//            this.notes = (String[]) map.get("Notes");
//            this.reference = (String) map.get("Reference");
//            this.stateid = (Integer) map.get("StateId");
//            this.timezone = (String) map.get("TimeZoneId");
//            this.typeid = (Integer) map.get("TypeId");
//
//            return true;
//        }
//
//    }
//
//    protected class ISAHubTag extends ISAWSObject {
//
//        protected String address;
//        protected boolean deleted;
//        protected int deviceTypeId;
//        protected int fixedCostId;
//        protected int id;
//        protected int index;
//        protected int interval;
//        protected boolean isCommunicating;
//        protected String kitReference;
//        protected String localId;
//        protected String[] moduleTypes;
//        protected String name;
//        protected String reference;
//        protected int state;
//        protected String tariff;
//        protected int tariffId;
//        protected int unitId;
//
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("Address", this.address);
//            this.object.put("Deleted", this.deleted);
//            this.object.put("DeviceTypeId", this.deviceTypeId);
//            this.object.put("FixedCostId", this.fixedCostId);
//            this.object.put("Id", this.id);
//            this.object.put("Index", this.index);
//            this.object.put("Interval", this.interval);
//            this.object.put("IsCommunicating", this.isCommunicating);
//            this.object.put("KitReference", this.kitReference);
//            this.object.put("LocalId", this.localId);
//            this.object.put("ModuleTypes", this.moduleTypes);
//            this.object.put("Name", this.name);
//            this.object.put("Reference", this.reference);
//            this.object.put("State", this.state);
//            this.object.put("Tariff", this.tariff);
//            this.object.put("TariffId", this.tariffId);
//            this.object.put("UnitId", this.unitId);
//
//            return super.toJSON();
//        }
//
//        @Override
//        public boolean fromJSON(String json) {
//            Map<?, ?> map = (Map<?, ?>) ITLClientUtilities.parseJSONResponseToMap(json).get("List");
//
//            this.address = (String) this.object.get("Address");
//            this.deleted = (Boolean) this.object.get("Deleted");
//            this.deviceTypeId = (Integer) this.object.get("DeviceTypeId");
//            this.fixedCostId = (Integer) this.object.get("FixedCostId");
//            this.id = (Integer) this.object.get("Id");
//            this.index = (Integer) this.object.get("Index");
//            this.interval = (Integer) this.object.get("Interval");
//            this.isCommunicating = (Boolean) this.object.get("IsCommunicating");
//            this.kitReference = (String) this.object.get("KitReference");
//            this.localId = (String) this.object.get("LocalId");
//            this.moduleTypes = (String[]) this.object.get("ModuleTypes");
//            this.name = (String) this.object.get("Name");
//            this.reference = (String) this.object.get("Reference");
//            this.state = (Integer) this.object.get("State");
//            this.tariff = (String) this.object.get("Tariff");
//            this.tariffId = (Integer) this.object.get("TariffId");
//            this.unitId = (Integer) this.object.get("UnitId");
//
//            return true;
//        }
//
//    }
//
//    protected class ISADeviceTag extends ISAWSObject {
//
//        protected boolean active;
//        protected boolean communicate;
//        protected long created;
//        protected boolean deleted;
//        protected int deviceId;
//        protected int id;
//        protected int index;
//        protected int internalAddress;
//        protected String locationId;
//        protected boolean manual;
//        protected String name;
//        protected int tagTypeId;
//        protected int unitId;
//        protected boolean validate;
//        protected String virtualField;
//        protected boolean visible;
//
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("Active", this.active);
//            this.object.put("Communicate", this.communicate);
//            this.object.put("Created", this.created);
//            this.object.put("Deleted", this.deleted);
//            this.object.put("DeviceId", this.deviceId);
//            this.object.put("Id", this.id);
//            this.object.put("Index", this.index);
//            this.object.put("InternalAddress", this.internalAddress);
//            this.object.put("LocationId", this.locationId);
//            this.object.put("Manual", this.manual);
//            this.object.put("Name", this.name);
//            this.object.put("TagTypeId", this.tagTypeId);
//            this.object.put("UnitId", this.unitId);
//            this.object.put("Validate", this.validate);
//            this.object.put("VirtualField", this.virtualField);
//            this.object.put("Visible", this.visible);
//
//            return super.toJSON();
//        }
//
//        @Override
//        public boolean fromJSON(String json) {
//            Map<?, ?> map = (Map<?, ?>) ITLClientUtilities.parseJSONResponseToMap(json).get("List");
//
//            this.active = (Boolean) this.object.get("Active");
//            this.communicate = (Boolean) this.object.get("Communicate");
//            this.created = (Long) this.object.get("Created");
//            this.deleted = (Boolean) this.object.get("Deleted");
//            this.deviceId = (Integer) this.object.get("DeviceId");
//            this.id = (Integer) this.object.get("Id");
//            this.index = (Integer) this.object.get("Index");
//            this.internalAddress = (Integer) this.object.get("InternalAddress");
//            this.locationId = (String) this.object.get("LocationId");
//            this.manual = (Boolean) this.object.get("Manual");
//            this.name = (String) this.object.get("Name");
//            this.tagTypeId = (Integer) this.object.get("TagTypeId");
//            this.unitId = (Integer) this.object.get("UnitId");
//            this.validate = (Boolean) this.object.get("Validate");
//            this.virtualField = (String) this.object.get("VirtualField");
//            this.visible = (Boolean) this.object.get("Visible");
//
//            return true;
//        }
//    }
//
//    //TODO fill this down:
//    protected class ISAMeasurement extends ISAWSObject {
//
//        protected int cars;
//        protected String currencySymbol;
//        protected long date;
//        protected String granularity;
//        protected double read;
//        protected int readCarbon;
//        protected int readCurrency;
//        protected int tagId;
//        protected int trees;
//        protected int unitId;
//
//        @SuppressWarnings("unchecked")
//        public String toJSON() {
//            this.object.put("UnitId", this.unitId);
//
//            return super.toJSON();
//        }
//
//        @Override
//        public boolean fromJSON(String json) {
//            // TODO Auto-generated method stub
//            return false;
//        }
//    }
}
