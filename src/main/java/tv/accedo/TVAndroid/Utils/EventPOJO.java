package tv.accedo.TVAndroid.Utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPOJO {

	// private List<EventBase> eventList = new ArrayList<EventBase>();
	public static final Logger LOGGER = Logger.getLogger(EventPOJO.class);

	@SerializedName("meta")
	@Expose
	private Meta meta;

	@SerializedName("event_type")
	@Expose
	private String event_type = null;

	@SerializedName("ts")
	@Expose
	private String ts = null;

	@SerializedName("did")
	@Expose
	private String did = null;

	@SerializedName("uid")
	@Expose
	private String uid = null;

	@SerializedName("lang")
	@Expose
	private String lang = null;

	@SerializedName("net")
	@Expose
	private String net = null;

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getTimestamp() {
		return ts;
	}

	public void setTimestamp(String ts) {
		this.ts = ts;
	}

	public String getEventType() {
		return event_type;
	}

	public void setEventType(String event_type) {
		this.event_type = event_type;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setEventType(Meta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "EventPOJO [meta=" + meta + ", event_type=" + event_type + ", timestamp=" + ts + ", did=" + did
				+ ", uid=" + uid + ", lang=" + lang + ", net=" + net + "]";
	}

	public static String compareEvents(EventPOJO eventExcel, EventPOJO eventLogs, List<String> mandatoryFields)
			throws IllegalArgumentException, IllegalAccessException, SecurityException, ClassNotFoundException {
		boolean flag = false;
		int count = 0;
		String difference = "Difference in stats : ";
		if (eventLogs == null) {
			difference = difference + " No event stats present in logs \n";
			return difference;
		}

		Field[] fields = (eventExcel.getClass()).getDeclaredFields();

		for (Field field : fields) {
			if (field.getName().equals("meta")) {
				try {
					eventExcel.getMeta().getClass().getDeclaredFields();
				} catch (NullPointerException e) {
					LOGGER.info("meta not present");
					break;
				}

				Field[] fieldsMeta = (eventExcel.getMeta().getClass()).getDeclaredFields();
				for (Field fieldMeta : fieldsMeta) {
					LOGGER.info("Field name to be comapred in meta: " + fieldMeta.getName());
					fieldMeta.setAccessible(true);
					String field1 = (String) fieldMeta.get(eventExcel.getMeta());
					String field2 = (String) fieldMeta.get(eventLogs.getMeta());

					if (field1 != null && mandatoryFields.contains(fieldMeta.getName())) {
						if (!field1.equals(field2)) {
							++count;
							difference = difference + "On comparing two mandatory fields of meta- "
									+ fieldMeta.getName() + " event excel has value " + field1
									+ " event logs has value " + field2 + "\n";
						}
						LOGGER.info("On comparing two mandatory fields of meta- FIELD NAME" + fieldMeta.getName()
								+ " : event excel has value " + field1 + " and event logs has value " + field2 + "\n");
					} else if (field1 != null && !mandatoryFields.contains(fieldMeta.getName())) {
						LOGGER.info("Not null field of meta:- " + fieldMeta.getName() + " event excel has value "
								+ field1 + " , event logs has value " + field2 + "\n");
						if (!(field2 != null)) {
							++count;
							difference = difference + " Not null field of meta:- " + fieldMeta.getName()
									+ " event excel has value " + field1 + " , event logs has value " + field2 + "\n";
						}
					}
				}
			}

			else {
				LOGGER.info("Field name to be comapred in event: " + field.getName());
				field.setAccessible(true);
				try {
					if (field.get(eventLogs).getClass().toString().contains("org.apache.log4j.Logger")) {
						LOGGER.info("Ignoring LOGGER field");
					}

					else {
						String field1 = (String) field.get(eventExcel);
						String field2 = (String) field.get(eventLogs);

						if (field1 != null && mandatoryFields.contains(field.getName())) {
							if (!field1.equals(field2)) {
								++count;
								difference = difference + " On comparing two mandatory fields of - " + field.getName()
										+ " event excel has value " + field1 + "event logs has value " + field2 + "\n";
								LOGGER.info("MISMATCH: On comparing two mandatory fields of - " + field.getName()
										+ " event excel has value " + field1 + "event logs has value " + field2 + "\n");
							} else {
								LOGGER.info("MATCH: On comparing two mandatory fields of - " + field.getName()
										+ " event excel has value " + field1 + "event logs has value " + field2 + "\n");
							}
						} else if (field1 != null && !mandatoryFields.contains(field.getName())) {
							if (!(field2 != null)) {
								++count;
								LOGGER.info(
										"MISMATCH : Not null field : - " + field.getName() + "event excel has value "
												+ field1 + " , event log has value " + field2 + "\n");
								difference = difference + " Not null field : - " + field.getName()
										+ "event excel has value " + field1 + " , event log has value " + field2 + "\n";
							} else {
								LOGGER.info("MATCH: Not null field : - " + field.getName() + "event excel has value "
										+ field1 + " , event log has value " + field2);
								// difference=difference +" Not null field : - "+field.getName() + "event excel
								// has value "+ field1 + " , event log has value "+ field2 + "\n";
							}

						}

					}
				} catch (NullPointerException e) {

				}
			}
		}
		LOGGER.info("difference " + difference);

		if (count == 0) {
			return "No difference";
		}

		else {
			return difference;
		}

	}

}