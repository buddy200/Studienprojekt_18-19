package de.uni_stuttgart.informatik.sopra.sopraapp.Util;

import android.os.Bundle;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 04.12.17.
 * Mail: felix.burk@gmail.com
 *
 * Helper for the search function
 * this implements simple string matching
 */

public class SearchUtil {

    static String searchFor[] =  {
            "Name", "Owner", "State", "Date"
    };

    /**
     * does a field match a search input?
     * @param f Field
     * @param input search input
     * @return boolean
     */
    public static boolean matchesFieldSearch(Field f, String input){
        Bundle b = f.getBundle();

        //search for name
        if(!f.getName().contains(input)){

            //search for type
            if(! b.getSerializable("type").toString().contains(input)) {

                // search for county
                if(!f.getCounty().contains(input)) {

                    //fieldToAdd is type agrarian
                    if (b.containsKey("owner")) {
                        //search for states and owners
                        if (!b.getString("owner").contains(input)) {
                            return false;
                        }
                    }


                    //fieldToAdd is type damage field
                    if (b.containsKey("evaluator")) {
                        //search for evaluators
                        if (!b.getString("evaluator").contains(input)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String[] getSearchFor() {
        return searchFor;
    }


    public static void searchForType(ArrayList<Field> fields, String query, String type) {
    }

    /**
     * search implementation
     * @param
     */
    /*
    public void onSearchButtonClicked(String input) {
        Log.e(TAG, "Search for: " + input);

        // copy dataFromFields in search data listGeoPoints
        // we need a deep copy - because fields contain other fields
        ArrayList<Field> searchData = new ArrayList<>(dataManager.getFields());
        ArrayList<Field> resultData = new ArrayList<>();

        /**
         * not optimal and dirty way of searching
         * but it's fast to implement and probably enough for our use case
         * - ah and this is case sensitive right now... TODO
         */
    /*
        Iterator<Field> iter = searchData.iterator();
        while(iter.hasNext()){
            Field f = iter.next();

            if(SearchUtil.matchesFieldSearch(f, input)){
                resultData.add(f);
            }

            if(f instanceof AgrarianField){
                for(DamageField dmg : ((AgrarianField)f).getContainedDamageFields()){
                    if(SearchUtil.matchesFieldSearch(dmg,input)){
                        resultData.add(dmg);
                    }
                }
            }

        }

        if(resultData.size() != 0){
            ItemListDialogFragment.newInstance(resultData).show(getSupportFragmentManager(), "SearchList" );
        }else{
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nothing_found), Toast.LENGTH_SHORT).show();
        }

    }*/

}
