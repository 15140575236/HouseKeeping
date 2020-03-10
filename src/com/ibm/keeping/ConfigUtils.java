/* Copyright IBM Corp. 2016 */
package com.ibm.keeping;

import java.util.ResourceBundle;

/**
 * A utility class to read RDH config parameters from RDH config properties file
 * It may be replaced by SPDI config utility in the future
 * 
 * @author wangyul
 * @since 03/11/2019
 */
public class ConfigUtils
{
    private static ResourceBundle bundle = null;

    private static String BUNDLENAME = "rdhclientconfig";

    /**
     * Gets a resource bundle using the specified name
     * 
     * @return ResourceBundle
     */
    private static ResourceBundle getResourceBundle()
    {
        if (bundle == null)
        {
            bundle = ResourceBundle.getBundle(BUNDLENAME);
        }
        return bundle;
    }

    /**
     * Get specified bundle name
     * 
     * @return
     */
    public static String getBundleName()
    {
        return BUNDLENAME;
    }

    /**
     * set bundle name
     * 
     * @param name
     */
    public static void setBundleName(String name)
    {
        BUNDLENAME = name;
        bundle = null;
    }

    /**
     * Gets a string for the given key from specified resource bundle, if no
     * value is found, then retrun null
     * 
     * @param name
     * @return
     */
    public static String getProperty(String name)
    {
        if (name == null)
            return null;
        String value = null;
        try
        {
            value = getResourceBundle().getString(name).trim();
        } catch (Throwable ex)
        {
            System.out.println("Key '" + name + "'not found.");
        }
        return value;
    }

    /**
     * Fefresh Resource Bundle if new bundle resource is set
     */
    public static void refreshResourceBundle()
    {
        bundle = null;
        getResourceBundle();
    }

    /**
     * Gets a string for the given key from specified resource bundle, if no
     * value is found, then retrun defaultValue
     * 
     * @param name
     * @return
     */
    public static String getValue(String key, String defaultValue)
    {
        String value = getProperty(key);
        if ((value == null) || (value.trim().length() == 0))
        {
            value = defaultValue;
        }
        return value;
    }
}
