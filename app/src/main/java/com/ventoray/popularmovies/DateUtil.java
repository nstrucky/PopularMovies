package com.ventoray.popularmovies;

/**
 * Created by nicks on 10/21/2017.
 */

public class DateUtil {

    public static String formatDate(String dateString) {
        String date;

        if (dateString == null) {
            return null;
        }

        if (dateString.isEmpty()) {
            return null;
        }

        String year = getYear(dateString);
        String month = getMonth(dateString);
        String day = getDay(dateString);

        date = month + " " + day + ", " + year;

        return date;
    }

    private static String getYear(String dateString) {
        String year = null;
        if (dateString != null){
            if (dateString.length() > 4) {
                year = dateString.substring(0, 4);
            }
        }
        return year;
    }

    private static String getMonth(String dateString) {
        if (dateString == null) {
            return "Month Error";
        }

        if (dateString.isEmpty()) {
            return "String Month Error";
        }

        String monthNumber = dateString.substring(5, 7);
        int month = Integer.valueOf(monthNumber);

        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Month";
        }
    }


    private static String getDay(String dateString) {
        if (dateString == null) {
            return "Day Error";
        }

        if (dateString.isEmpty()) {
            return "String Day Error";
        }

        String dayNumber = dateString.substring(8);
        String secondDigit = dateString.substring(9);
        String suffix;
        int day = Integer.valueOf(dayNumber);
        int secondDigitInt = Integer.valueOf(secondDigit);

        if (day > 9 && day < 20) {
            suffix = "th";
        } else {
            switch (secondDigitInt) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
            }
        }
        return ""+day+suffix;
    }

}
