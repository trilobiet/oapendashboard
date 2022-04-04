package org.oapen.dashboard.management;

import static java.util.Collections.emptySet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;


/**
 * Converts a comma separated string to a Set and vice versa.
 * 
 * @author acdhirr
 *
 */
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
	
    private static final String SPLIT_CHAR = ",";
    
    @Override
    public String convertToDatabaseColumn(Set<String> stringList) {
    	
    	String[] strings = stringList.toArray(new String[0]);
        return StringUtils.isAllEmpty(strings) ? null: String.join(SPLIT_CHAR, stringList);
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {
        return string != null ? new HashSet<String>(Arrays.asList(string.split(SPLIT_CHAR))) : emptySet();
    }
}
