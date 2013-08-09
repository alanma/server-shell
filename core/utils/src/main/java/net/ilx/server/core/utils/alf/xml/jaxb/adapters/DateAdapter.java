package net.ilx.server.core.utils.alf.xml.jaxb.adapters;


public class DateAdapter extends DateTimeAdapter {

	public DateAdapter() {
		this("dd.MM.yyyy");
	}

	public DateAdapter(String format) {
		super(format);
	}
}