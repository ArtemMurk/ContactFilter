package mocks.request;

import static mocks.model.MockModel.GET_ALL_REGEXP;

public class MockRequests
{
    public static final String BASE_REQUEST = "http://localhost:8080/hello/contacts?nameFilter=";
    public static final String NOT_VALID_REGEXP = "*";

    public static final String GET_ALL_CONTACT_REQUEST = BASE_REQUEST +GET_ALL_REGEXP;
    public static final String NULL_CONTACT_REQUEST = BASE_REQUEST;
    public static final String PARSE_EXCEPTION_REQUEST = BASE_REQUEST+NOT_VALID_REGEXP;


}
