package com.itl_energy.webclient.instee.itl.client.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class for serialising/deserialising lists of resources.
 *
 * @author Bruce Stephen
 * @version 19th September 2013
 * @param <S> The class doing the serialising/deserialising.
 * @param <T> The class being serialised/deserialised.
 */
public class ListSerialized<S extends ResourceSerialized<T>, T> extends ResourceSerialized<List<T>> {

    public ListSerialized(List<T> m) {
        super(m);

        //this.object.put("observationTime",m.getObservationTime());
    }

    @Override
    public List<T> fromJSON(Map<?, ?> map) {
        LinkedList<?> list = (LinkedList<?>) map.get("items");
        List<T> lstT = new ArrayList<T>();
		//S deser=new S(null);

        for (Iterator<?> it = list.iterator(); it.hasNext();) {
            Object object = it.next();
			//T t=deser.fromJSON((LinkedHashMap<?,?>)object);

            //lstT.add(t);
        }

        return lstT;
    }
}
