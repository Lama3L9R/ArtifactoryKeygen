package icu.lama.artifactory.keygen;

import org.jfrog.license.a.ObfuscatedString;

import java.util.List;

public class Keygen {
    private static final List<Integer> jfrogKeys = List.of(
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCS7w4lUQgAxxQHVSdCRVZ5E+/18Rm4VMeiqgHCLPwCbAMZN0KPikzSCckpQQBp4XbDlCQ64WOGZ/R3/QLJbBqDqlE4n79Q8H/3DYod8IqsacbIk/sTOq1pqAMopVz+wTMJamABstPftibj1V7CMNR9qOSbVCs8lmVT78TeU0l0JwIDAQAB".hashCode(),
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmquSyL1wSs6DvU5iceNvbfha4MPX040iHFuoBTeEZA43rsHWLNLIECmVJ3Zv1xV9+NkrORKQEuJckEXzbTHSrpzZDfF/sjlKhalHKN3joNgIoIFG5MoM9kPFEB0mAJx/Hpiojj+/LZ5uTHIWiEGKm6C4EQQL9F+2FpcQbj6ve26t03YNZVIhzgmGw4TEb/1WXje7ywtMv3bGkKqAak6VoTKnn4MOm3ULzck9K+KPIgOd01Wa00PPbVqitB7Tqej7y3wZKMPxgzM0n7fouH7lu0yowiV+V5SyO/6g/Wq+DNgniKpnbcFsVLxFE7LcyHr94cAorBH+EUcepGKqqml4cQIDAQAB".hashCode(),
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArJfQO8KDTngzzSrvt5ZdZ6eI9f9EL0WBWQicBWfsu0hgEXds2uJ0KINDnVyInxpo+5OR+rh5ffwaOrO18zp/udWEooYZOSwGv1PtwFN/y8RRjeT3E/y4zGtKfkEHhpJsw4Tn3GcAhGxzQBvuNbmDuB2g/H8AlITB9yMnYo2J0DinLmQSQCgFmgOY6QJI6GqL4Rf2dnERKTfqOtYGhKaRJLfGEU4Z1q+YURQ9QT7kbRD6889ODwQHrBhdISBlcM8GNW09kSPFjc79DKQlS5Wsl5b0PY261i6YS7cAB0tlRoyL93ZTEnOCRdY42bkVtmReNU7kXMy7y0fDBhCqjMENAQIDAQAB".hashCode(),
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRlDUxb2UERz5o+4mVyBn0RHMJUftlFnIOBYDZvmpNheaxyLjkgP05o5IzAmCWexCJNaykcY5un2sdolb20mf6TlGo/OmDibr83L8mcZoqqbQBmAqtzPtIMY4ZCSKHrctyJs8MAUvvmxKFrnXo8ivexec9/HVfExwR/TTaMhBGMQIDAQAB".hashCode()
    );
    public static void main(String... args) {
        jfrogKeys.forEach(System.out::println);
    }
}
