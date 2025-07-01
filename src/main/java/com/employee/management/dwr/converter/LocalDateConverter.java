package com.employee.management.dwr.converter;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter implements Converter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    protected ConverterManager converterManager = null;

    public ConverterManager getConverterManager() {
        return converterManager;
    }

    public void setConverterManager(ConverterManager converterManager) {
        this.converterManager = converterManager;
    }

    @Override
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException {
        String value = data.getValue();
        if (value == null || value.isBlank() || "null".equals(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ConversionException(paramType, "Failed to convert inbound value '" + value + "' to LocalDate", e);
        }
    }

    @Override
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException {
        if (data == null) {
            return new NonNestedOutboundVariable("");
        }
        LocalDate date = (LocalDate) data;
        String jsString = "\"" + FORMATTER.format(date) + "\"";
        return new NonNestedOutboundVariable(jsString);
    }
}
