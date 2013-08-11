package net.ilx.server.shell.core.utils.alf.xml.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EuroCentsAdapter  extends XmlAdapter<String, Integer> {

	@Override
	public Integer unmarshal(String v) throws Exception {
		return Integer.valueOf(v);
	}

	@Override
	public String marshal(Integer v) throws Exception {
		return v.toString();
	}

}
