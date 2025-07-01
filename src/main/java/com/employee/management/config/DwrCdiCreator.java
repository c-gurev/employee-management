package com.employee.management.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

import javax.enterprise.inject.spi.CDI;

public class DwrCdiCreator extends AbstractCreator implements Creator {

    private static final Log log = LogFactory.getLog(DwrCdiCreator.class);

    private Class<?> clazz = null;

    public DwrCdiCreator() {
    }

    public void setClass(String classname) {
        try {
            this.clazz = LocalUtil.classForName(classname);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Class not found: " + classname, ex);
        }
    }

    public Class<?> getType() {
        if (this.clazz == null) {
            try {
                this.clazz = this.getInstance().getClass();
            } catch (InstantiationException e) {
                log.error("Failed to instantiate object", e);
                return Object.class;
            }
        }
        return this.clazz;
    }

    @Override
    public Object getInstance() throws InstantiationException {
        return CDI.current().select(clazz).get();
    }
}
