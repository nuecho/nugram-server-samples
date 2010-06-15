/*
 * Copyright (c) 2009-2010 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.samples.grammars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nuecho.api.grammar.CacheControl;
import com.nuecho.api.grammar.ContextInitializationException;
import com.nuecho.api.grammar.ContextInitializer;
import com.nuecho.api.grammar.GrammarFragmentFactory;
import com.nuecho.api.grammar.NotModifiedException;

public class BillPayeeList implements ContextInitializer
{
    private Map<String, Company> mDatabase = new HashMap<String, Company>();

    public BillPayeeList() throws IOException
    {
        readEntriesFromCsvFile();
    }

    /*
      Implementation of the ContextInitializer interface.
     */
    @SuppressWarnings("unchecked")
    public Map getContext(Map parameters, GrammarFragmentFactory factory, CacheControl cacheControl)
            throws ContextInitializationException, NotModifiedException
    {      
        // 1. Create the instantiation context object. 
        HashMap<String, Object> context = new HashMap<String, Object>();
        
        // 2. Populate the context with entries taken from the HTTP query parameters.
        //    The 'symbol' parameter can be specified more than once.   
        String[] symbols = (String[]) parameters.get("symbol");
        if (symbols != null)
        {
            List<Company> entries = new ArrayList<Company>();
            for (String symbol : symbols)
            {
                Company entry = mDatabase.get(symbol);
                if (entry != null)
                {
                    entries.add(entry);
                }
            }
            // Put the variable 'entries' in the instantiation context.
            context.put("entries", entries);
        }
        else
        {
            throw new ContextInitializationException("no symbols specified");
        }
        
        // 3. Return the instantiation context.
        return context;
    }

    
    /*
     * Read a CSV file containing all the entries in the database. The CSV file is read from
     * the classpath.
     * 
     * The format of each line in the file is:
     *   company name,symbol,alias1;alias2;alias3;...
     * 
     * @throws IOException
     */
    private void readEntriesFromCsvFile() throws IOException
    {
        InputStream inputStream = this.getClass()
                                      .getClassLoader()
                                      .getResourceAsStream("com/nuecho/samples/grammars/companies.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.ready())
        {
            String line = reader.readLine();
            String[] values = line.split(",");
            if (values.length < 2) continue;

            Company entry = new Company(values[0], values[1], values.length >= 3
                    ? values[2].split(";")
                    : new String[]{});
            mDatabase.put(entry.getSymbol(), entry);
        }
    }

    /*
     * Company database entry. 
     */
    public static class Company
    {
        String mName;
        String mSymbol;
        String[] mAliases;

        public Company(String name, String symbol, String[] aliases)
        {
            super();
            mName = name;
            mAliases = aliases;
            mSymbol = symbol;
        }

        public String getName()
        {
            return mName;
        }

        public String getSymbol()
        {
            return mSymbol;
        }

        public String[] getAliases()
        {
            return mAliases;
        }

    }
}

