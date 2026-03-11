package com.mycompany.cnp;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CnpParser {
    
    private static final String VALID_CONST = "279146358279";
    private static final String INVALID_ERROR = "invalid";
    private static final Map<String, String> COUNTY_MAP = new HashMap<>();
    static {
        COUNTY_MAP.put("01", "Alba");
        COUNTY_MAP.put("02", "Arad");
        COUNTY_MAP.put("03", "Arges");
        COUNTY_MAP.put("04", "Bacau");
        COUNTY_MAP.put("05", "Bihor");
        COUNTY_MAP.put("06", "Bistrita-Nasaud");
        COUNTY_MAP.put("07", "Botosani");
        COUNTY_MAP.put("08", "Brasov");
        COUNTY_MAP.put("09", "Braila");
        COUNTY_MAP.put("10", "Buzau");
        COUNTY_MAP.put("11", "Caras-Severin");
        COUNTY_MAP.put("12", "Cluj");
        COUNTY_MAP.put("13", "Constanta");
        COUNTY_MAP.put("14", "Covasna");
        COUNTY_MAP.put("15", "Dambovita");
        COUNTY_MAP.put("16", "Dolj");
        COUNTY_MAP.put("17", "Galati");
        COUNTY_MAP.put("18", "Gorj");
        COUNTY_MAP.put("19", "Harghita");
        COUNTY_MAP.put("20", "Hunedoara");
        COUNTY_MAP.put("21", "Ialomita");
        COUNTY_MAP.put("22", "Iasi");
        COUNTY_MAP.put("23", "Ilfov");
        COUNTY_MAP.put("24", "Maramures");
        COUNTY_MAP.put("25", "Mehedinti");
        COUNTY_MAP.put("26", "Mures");
        COUNTY_MAP.put("27", "Neamt");
        COUNTY_MAP.put("28", "Olt");
        COUNTY_MAP.put("29", "Prahova");
        COUNTY_MAP.put("30", "Satu Mare");
        COUNTY_MAP.put("31", "Salaj");
        COUNTY_MAP.put("32", "Sibiu");
        COUNTY_MAP.put("33", "Suceava");
        COUNTY_MAP.put("34", "Teleorman");
        COUNTY_MAP.put("35", "Timis");
        COUNTY_MAP.put("36", "Tulcea");
        COUNTY_MAP.put("37", "Vaslui");
        COUNTY_MAP.put("38", "Valcea");
        COUNTY_MAP.put("39", "Vrancea");
        COUNTY_MAP.put("40", "Bucuresti");
        COUNTY_MAP.put("41", "Bucuresti - Sector 1");
        COUNTY_MAP.put("42", "Bucuresti - Sector 2");
        COUNTY_MAP.put("43", "Bucuresti - Sector 3");
        COUNTY_MAP.put("44", "Bucuresti - Sector 4");
        COUNTY_MAP.put("45", "Bucuresti - Sector 5");
        COUNTY_MAP.put("46", "Bucuresti - Sector 6");
        COUNTY_MAP.put("47", "Bucuresti - Sector 7 (desfiintat)");
        COUNTY_MAP.put("48", "Bucuresti - Sector 8 (desfiintat)");
        COUNTY_MAP.put("51", "Calarasi");
        COUNTY_MAP.put("52", "Giurgiu");
        COUNTY_MAP.put("70", "Unknown");
    } // COUNTRY MAP DICTIONARY
    
    public static void main(String[] args) {}

    public CnpParser() {}
    
    public static CNP parseCNP(String rawCNP){
  
        if( rawCNP == null || !rawCNP.matches("^\\d{13}$") ){
            return createInvalidCNP();
        }
        
        if( !isValidCheckSum(rawCNP) ){
            return createInvalidCNP();
        }
        
        CNP result = new CNP();
        String gender = extractGender(rawCNP);
        String birthDate = extractBirthDate(rawCNP);
        String county = extractCounty(rawCNP);
        String nationality = extractNationality(rawCNP);
        String serialCode = extractSerialCode(rawCNP);
        
        if( gender.equals(INVALID_ERROR) || birthDate.equals(INVALID_ERROR) || 
            county.equals(INVALID_ERROR) || nationality.equals(INVALID_ERROR) ){
            return createInvalidCNP();
        }
        
        result.setGender(gender);
        result.setBirthDate(birthDate);
        result.setCountyName(county);
        result.setNationality(nationality);
        result.setSerialCode(serialCode);
        result.setValid(true);
        
        return result;
    }
    
    private static CNP createInvalidCNP(){
        CNP invalid = new CNP();
        invalid.setValid(false);
        return invalid;
    }
    
    private static boolean isValidCheckSum(String rawCNP) {
        if (rawCNP == null || rawCNP.length() != 13) return false;

        int[] weights = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};
        int sum = 0;

        for (int i = 0; i < 12; i++) {
            // Subtracting '0' is often safer than getNumericValue in some environments
            int digit = rawCNP.charAt(i) - '0';
            sum += digit * weights[i];
        }

        int remainder = sum % 11;
        int expectedLastDigit = (remainder == 10) ? 1 : remainder;
        int actualLastDigit = rawCNP.charAt(12) - '0';

        return expectedLastDigit == actualLastDigit;
    }
    
    public static String extractGender(String rawCNP){
        char genderDigit = rawCNP.charAt(0);
        return switch (genderDigit) {
            case '1', '3', '5', '7' -> "Male";
            case '2', '4', '6', '8' -> "Female";
            case '9'                -> "Unknown";
            default                 -> "invalid";
        };
    }
    
    public static String extractBirthDate(String rawCNP) {
        char genderDigit = rawCNP.charAt(0);
        String aa = rawCNP.substring(1, 3);
        String mm = rawCNP.substring(3, 5);
        String dd = rawCNP.substring(5, 7);

        String century = switch(genderDigit) {
            case '1', '2', '7', '8' -> "19";
            case '3', '4'           -> "18";
            case '5', '6'           -> "20";
            case '9'                -> "xx";
            default                 -> "00";
        };

        String fullDate = century + aa + "-" + mm + "-" + dd;
        
        try {
            LocalDate.parse(fullDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return fullDate;
        }
        catch (DateTimeParseException e) {
            return "invalid";
        }
    }
       
    public static String extractCounty(String rawCNP){
        String countyCode = rawCNP.substring(7, 9);
        return COUNTY_MAP.getOrDefault(countyCode, "invalid");
    }
    
    public static String extractNationality(String rawCNP){
        char nationalityDigit = rawCNP.charAt(0);
        
        return switch(nationalityDigit){
            case '1', '2', '3', '4', '5', '6' -> "Romanian";
            case '7', '8'                     -> "Resident";
            case '9'                          -> "Foreigner";
            default                           -> "invalid";
        };
    }
    
    private static String extractSerialCode(String rawCNP){
        return ("#" + rawCNP.substring(9, 12));
    }
    
}
