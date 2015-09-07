import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.codehaus.jackson.JsonProcessingException;

import java.io.IOException;
import java.util.*;

public class DeskeroApiShooter {

    private final static String api_url = "https://api.deskero.com/";
    private final static String api_token = "NTMwZDk4OTBlNGIwYWU2MjM4NGI0ZGQ0OmJkOWE5YTU1LTZjMGItNDY5YS04MTA1LTQ1ZDM0ZTYyNGJlZg==";
    private final static String client_id = "530d9890e4b0ae62384b4dd4";
	private final static String path_to_file = "/path/to/file.txt";

    public static void main(String[] args)
    {
        try {
            //Set Jackson ObjectMapper for Unirest
            Unirest.setObjectMapper(new ObjectMapper() {

                private org.codehaus.jackson.map.ObjectMapper jacksonObjectMapper = new org.codehaus.jackson.map.ObjectMapper();

                @Override
                public <T> T readValue(String value, Class<T> valueType) {
                    try {
                        return jacksonObjectMapper.readValue(value, valueType);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public String writeValue(Object value) {
                    try {
                        return jacksonObjectMapper.writeValueAsString(value);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });

			HttpResponse<JsonNode> authResponse = getAccessToken();
			String accessToken = "";
			if(authResponse.getStatus() < 400)
			{
				accessToken = authResponse.getBody().getObject().get("access_token").toString();
				System.out.println(authResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + authResponse.getBody());
			}


			HttpResponse<JsonNode> ticketListResponse = getTicketList(accessToken);
			if(ticketListResponse.getStatus() < 400)
			{
				System.out.println(ticketListResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + ticketListResponse.getBody());
			}

            Map<String, String> searchFields = new HashMap<>();
            searchFields.put("openedBy", "id=5458d615e4b02bed71bfb79b");
			HttpResponse<JsonNode> ticketSearchResponse = getTicketSearch(accessToken, searchFields);
			if(ticketSearchResponse.getStatus() < 400)
			{
				System.out.println(ticketSearchResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + ticketSearchResponse.getBody());
			}

            String ticketId = "55e8664de4b0f88f425a2274";
			HttpResponse<JsonNode> ticketDetailResponse = getTicketDetail(accessToken, ticketId);
			if(ticketDetailResponse.getStatus() < 400)
			{
				System.out.println(ticketDetailResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + ticketDetailResponse.getBody());
			}

            Map<String, Object> replyData = new HashMap<>();
            replyData.put("text", "Problem solved!");
            Map<String, Object> replyFromOperator = new HashMap<>();
            replyFromOperator.put("id", "530f5c03e4b0902c2e1dad40");
            replyData.put("replyFromOperator", replyFromOperator);
            List<Object> attachedDocuments = new ArrayList<>();

            Map<String, Object> attachedDocument_1 = new HashMap<>();
            attachedDocument_1.put("documentName", "doc.txt");
            attachedDocument_1.put("documentType", "text/plain");
            attachedDocument_1.put("documentBlob", FileToHex.convertToHex(path_to_file));

            Map<String, Object> attachedDocument_2 = new HashMap<>();
            attachedDocument_2.put("documentName", "doc.txt");
            attachedDocument_2.put("documentType", "text/plain");
            attachedDocument_2.put("documentBase64", FileToBase64.encodeFileToBase64Binary(path_to_file));

            attachedDocuments.add(attachedDocument_1);
            attachedDocuments.add(attachedDocument_2);

            replyData.put("attachedDocuments", attachedDocuments);
			HttpResponse<JsonNode> addReplyResponse = addReplay(accessToken, ticketId, replyData);
			if(addReplyResponse.getStatus() < 400)
			{
				System.out.println(addReplyResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + addReplyResponse.getBody());
			}

            Map<String, Object> ticketOpenedBy = new HashMap<>();
            ticketOpenedBy.put("id", "558bc15ee4b0da42b588bf26");

            Map<String, Object> ticketType = new HashMap<>();
            ticketType.put("id", "50913dd4c2e67797f82e5bb2");

            List<Object> replayList = new ArrayList<>();
            replayList.add(replyData);

            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("subject", "Ticket from API");
            ticketData.put("description", "Ticket description");
            ticketData.put("openedBy", ticketOpenedBy);
            ticketData.put("type", ticketType);
            ticketData.put("attachedDocuments", attachedDocuments);
            ticketData.put("replies", replayList);

            replyData.put("replyDate", "1382455565000");
			HttpResponse<JsonNode> createdTicketResponse = createTicket(accessToken, ticketData);
			if(createdTicketResponse.getStatus() < 400)
			{
				System.out.println(createdTicketResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + createdTicketResponse.getBody());
			}

			HttpResponse<JsonNode> updatedTicketIdResponse = updateTicket(accessToken, ticketId, ticketData);
			if(updatedTicketIdResponse.getStatus() < 400)
			{
				System.out.println(updatedTicketIdResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + updatedTicketIdResponse.getBody());
			}

			HttpResponse<JsonNode> deletedTicketIdResponse = deleteTicket(accessToken, ticketId);
			if(deletedTicketIdResponse.getStatus() < 400)
			{
				System.out.println(deletedTicketIdResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + deletedTicketIdResponse.getBody());
			}

            Map<String, Object> noteData = new HashMap<>();
            noteData.put("note", "Resolve this!");
            noteData.put("insertBy", "530d9890e4b0ae62384b4dd1");
            noteData.put("assignedTo", "530d9890e4b0ae62384b4dd1");
            noteData.put("date", "1388144315000");
            noteData.put("remindMe", "1388144315000");

			HttpResponse<JsonNode> createdNoteIdResponse = addNote(accessToken, ticketId, noteData);
			if(createdNoteIdResponse.getStatus() < 400)
			{
				System.out.println(createdNoteIdResponse.getBody());
			}
			else
			{
				System.out.println("Error: " + createdNoteIdResponse.getBody());
			}

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    /*
	Title: obtaining Access Token using Deskero API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/authentication
	Request type : GET
	Request URI  : https://api.deskero.com/oauth/token?grant_type=client_credentials
	Headers :
		Authorization : Basic {{apiToken}}
	Parameters :

			field name 	: grant_type
			field value : client_credentials
			required 	: true
			default 	:

	Exemple response:
		response:
			{
			  "access_token": "58cfg61g-1585-5c72-g35f-7fe76893ed2f",
			  "token_type": "bearer",
			  "expires_in": 1398616,
			  "scope": "trust write read"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> getAccessToken() throws UnirestException {
        return Unirest.get(api_url + "oauth/token?grant_type=client_credentials")
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + api_token)
                .asJson();
    }

    /*
	Title: list of a tickets using Deskero's API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : GET
	Request URI  : https://api.deskero.com/ticket/list
	Headers :
		Authorization : Bearer {{bearer}}
		Accept 		  : application/json
		clientId 	  : {{clientId}}
	Parameters :

		field name 	: page
		field value : int value for page to show
		required 	: false
		default 	: 1


	Example response:
		response:
			{
			  "ticket": {
			    "totalRecords": 50,
			    "recordsPerPage": 25,
			    "previousQuery": null,
			    "nextQuery": "https://api.deskero.com/ticket/list?page=2",
			    "records": [
			      See ticket detail
			    ]
			  }
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> getTicketList(String accessToken, int page) throws UnirestException {
        return Unirest.get(api_url + "ticket/list?page=" + page)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .asJson();
    }

    public static HttpResponse<JsonNode> getTicketList(String accessToken) throws UnirestException {
        return getTicketList(accessToken, 1);
    }

    /*
	Title: search tikets by fields, using Deskero`s API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : GET
	Request URI  : https://api.deskero.com/ticket/list
	Headers :
		Authorization : Bearer {{bearer}}
		Accept 		  : application/json
		clientId 	  : {{clientId}}
	Parameters :

		field name 	: page
		field value : int value for page to show
		required 	: false
		default 	: 1

		field name 	: {{fieldName}}
		field value : a value to search
		required 	: false
		default 	:
	Example response:
		response:
			{
			  "ticket": {
			    "totalRecords": 4,
			    "recordsPerPage": 25,
			    "previousQuery": null,
			    "nextQuery": null,
			    "records": [
			      See ticket detail
			    ]
			  }
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All

	Fields:

		field name	: id
		description : auto-generated ticket id

		field name	: number
		description : auto-generated ticket number, number/year

		field name	: subject
		description : subject

		field name	: description
		description : complete text

		field name	: insertDate
		description : insert date timestamp

		field name	: managedDate
		description : last edit/reply/change status date timestamp

		field name	: closedDate
		description : close date timestamp

		field name	: priority
		description : int priority value, 0 or 1

		field name	: assignedTo
		description : agent who ticket must be assigned, see agent detail

		field name	: cc
		description : list of cc agent, see agent detail

		field name	: openedBy
		description : customer who opened the ticket, see customer detail

		field name	: numberOfReplies
		description : int replies number

		field name	: replies
		description : list of replies, see reply detail

		field name	: tags
		description : list of tags, see tag detail

		field name	: attachedDocuments
		description : list of attached documents, see attached document detail

		field name	: status
		description : ticket status (opened, closed, on hold, solved), see status detail

		field name	: type
		description : ticket type, see ticket type detail

		field name	: area
		description : ticket area, see ticket area detail

		field name	: group
		description : ticket group, see ticket group detail

		field name	: source
		description : ticket source, see source detail

		field name	: memo
		description : text note

		field name	: customFields
		description : list of custom fields, see custom field detail

		field name	: tweetId
		description : tweet id, if reply comes from twitter

		field name	: tweetType
		description : mention or direct message

		field name	: facebookId
		description : post id, if reply comes from facebook

		field name	: facebookType
		description : post or private message

		field name	: linkedinId
		description : feed id, if reply comes from linkedin

		field name	: linkedinType
		description : ?

		field name	: googlePlusId
		description : activity id, if reply comes from google plus

		field name	: googlePlusType
		description : ?

		field name	: youtubeId
		description : video id, if reply comes from youtube

		field name	: youtubeType
		description : ?

		field name	: messageUniqueId
		description : mail unique id, if reply comes from email

		field name	: customPortalId
		description : id of custom portal

		field name	: toReply
		description : agent have to reply to this ticket

		field name	: toReplyCustomer
		description : customer have to reply to this ticket

    */
    public static HttpResponse<JsonNode> getTicketSearch(String accessToken, int page, Map<String, String> searchFields) throws UnirestException {
        String query = "?";

        for (Map.Entry<String, String> entry : searchFields.entrySet()) {
            query += entry.getKey() + "._" + entry.getValue() + "&";
        }

        return Unirest.get(api_url + "ticket/list" + query + "page=" + page)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .asJson();
    }

    public static HttpResponse<JsonNode> getTicketSearch(String accessToken, Map<String, String> searchFields) throws UnirestException {
        return getTicketSearch(accessToken, 1, searchFields);
    }

    /*
	Title: show of a ticket using Deskero's API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : GET
	Request URI  : https://api.deskero.com/ticket/{{ticketId}}
	Headers :
		Authorization : Bearer {{bearer}}
		Accept 		  : application/json
		clientId 	  : {{clientId}}
	Example response:
		response:
			{
				"id": "5277c21ce4b0d3bd74d43841",
				"number": "2015/2013",
				"subject": "Problem with on-line order!",
				"description": "Hello, I've just ordered from your on-line catalogue the complete hardback collection on Little Nemo in Slumberland, by Winsor McCay. Despite being available on your inventory, I keep getting an \"item not found\" error, and can't complete my order. Is there something you can do?",
				"insertDate": 1383580226312,
				"managedDate": 1383580226397,
				"closedDate": null,
				"priority": 0,
				"assignedTo": {
				  See agent detail
				},
				"cc": [],
				"openedBy": {
				  See customer detail
				},
				"numberOfReplies": 1,
				"replies": [
				  See reply detail
				],
				"tags": [
				  See tag detail
				],
				"attachedDocuments": [
				  See attached document detail
				],
				"status": {
				  See ticket status detail
				},
				"type": {
				  See ticket type detail
				},
				"area": {
				  See ticket area detail
				},
				"group": {
				  See ticket group detail
				},
				"memo": null,
				"source": {
				  See ticket source detail
				},
				"customFields": [
				  See custom field detail
				],
				"ticketNotes": [
				  See ticket note detail
				],
		  		"tweetId": null,
		  		"tweetType": null,
		  		"facebookId": null,
		  		"facebookType": null,
		  		"linkedinId": null,
		  		"linkedinType": null,
		  		"googlePlusId": null,
		  		"googlePlusType": null,
		  		"youtubeId": null,
		  		"youtubeType": null,
		  		"messageUniqueId": null,
		  		"customPortalId": null,
		  		"toReply": false,
		  		"toReplyCustomer": false
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> getTicketDetail(String accessToken, String ticketId) throws UnirestException {
        return Unirest.get(api_url + "ticket/" + ticketId)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .asJson();
    }

    /*
	Title: adds a reply for the ticket using Deskero`s API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : PUT
	Request URI  : https://api.deskero.com/ticket/reply/{{ticketId}}
	Headers :
		Authorization : Bearer {{bearer}}
		Content-Type : application/json
		clientId : {{clientId}}
	Example response:
		description : Reply id
		response 	:
			{
			  "id": "51e56642e4b0f36e576a8f74"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> addReplay(String accessToken, String ticketId, Object reply) throws UnirestException {
        return Unirest.put(api_url + "ticket/reply/" + ticketId)
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .body(reply)
                .asJson();
    }

    /*
	Title: create of a ticket using Deskero's API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : POST
	Request URI  : https://api.deskero.com/ticket/insert
	Headers :
		Authorization : Bearer {{bearer}}
		Content-Type : application/json
		clientId : {{clientId}}
	Example response:
		desctiption : Created ticket id
		response 	:
			{
			  "id": "5277c21ce4b0d3bd74d43841"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> createTicket(String accessToken, Object ticket) throws UnirestException {
        return Unirest.post(api_url + "ticket/insert")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .body(ticket)
                .asJson();
    }

    /*
	Title: update of a ticket using Deskero's API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : PUT
	Request URI  : https://api.deskero.com/ticket/update/{{ticketId}}
	Headers :
		Authorization : Bearer {{bearer}}
		Content-Type : application/json
		clientId : {{clientId}}
	Example response:
		description : Updated ticket id
		response 	:
			{
			  "id": "5277c21ce4b0d3bd74d43841"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> updateTicket(String accessToken, String ticketId, Object ticket) throws UnirestException    {
        return Unirest.put(api_url + "ticket/update/" + ticketId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .body(ticket)
                .asJson();
    }

    /*
	Title: delete of a ticket using Deskero's API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : DELETE
	Request URI  : https://api.deskero.com/ticket/delete/{{ticketId}}
	Headers :
		Authorization : Bearer {{bearer}}
		Content-Type : application/json
		clientId : {{clientId}}
	Example response:
		desctiption : Deleted ticket id
		response 	:
			{
			  "id": "5277c21ce4b0d3bd74d43841"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> deleteTicket(String accessToken, String ticketId) throws UnirestException {
        return Unirest.delete(api_url + "ticket/delete/" + ticketId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .asJson();
    }

    /*
	Title: adds a note for the ticket using Deskero`s API
	Author: Deskero support team, for any information or request about this code please send an email to support@deskero.com.
	Look reference of that section: http://www.deskero.com/en/documentation/api/tickets
	Request type : PUT
	Request URI  : https://api.deskero.com/ticket/addNote/{{ticketId}}
	Headers :
		Authorization : Bearer {{bearer}}
		Content-Type : application/json
		clientId : {{clientId}}
	Example response:
		description : Note id
		response 	:
			{
			  "id": "52bd6d4803647785e90abb34"
			}
		the possible response codes:

			HTTP Code 	: 200 OK
			Description : Operation completed
			Calls 		: List, Search, Detail, Update, Delete, Reply

			HTTP Code 	: 201 CREATED
			Description : Element successfully inserted
			Calls 		: Create

			HTTP Code 	: 204 NO CONTENT
			Description : No elements retrieved with a list, search or detail call
			Calls 		: List, Search, Detail

			HTTP Code 	: 400 BAD REQUEST
			Description : Validation error or field name mismatch
			Calls 		: All

			HTTP Code 	: 500 INTERNAL SERVER ERROR
			Description : An internal exception does not allow to complete the requested operation
			Calls 		: All
    */
    public static HttpResponse<JsonNode> addNote(String accessToken, String ticketId, Object note) throws UnirestException {
        return Unirest.put(api_url + "ticket/addNote/" + ticketId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("clientId", client_id)
                .body(note)
                .asJson();
    }
}








