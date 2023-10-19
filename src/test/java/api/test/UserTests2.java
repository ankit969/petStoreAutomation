package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests2 {
	
	Faker faker;
	User userpayload;
	public Logger logger;
	
	@BeforeClass
	public void setUp() {
		faker = new Faker();//faker to generate fake data
		userpayload = new User();
		
		userpayload.setId(faker.idNumber().hashCode());//hashCode to generate random number
		userpayload.setUsername(faker.name().username());
		userpayload.setFirstName(faker.name().firstName());
		userpayload.setLastName(faker.name().lastName());
		userpayload.setEmail(faker.internet().safeEmailAddress());
		userpayload.setPassword(faker.internet().password(5, 10));
		userpayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger = LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1)
	public void testPostUser() {
		logger.info("**** Create User ********");
		
		Response response = UserEndPoints2.createUser(userpayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		//Assert.assertEquals(response.getBody(), "");
		
		logger.info("**** User is Created ********");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("**** Reading User Info ********");
		
		Response response = UserEndPoints2.readUser(this.userpayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("**** User Info is displayed ********");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() {
		logger.info("**** Updating User ********");
		
		//update data using payload
		userpayload.setFirstName(faker.name().firstName());
		userpayload.setLastName(faker.name().lastName());
		userpayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints2.updateUser(this.userpayload.getUsername(), userpayload);
		response.then().log().all();//or
		//response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Check the data after update
		Response responseAfterUpdate = UserEndPoints.readUser(this.userpayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
		logger.info("**** User is Updated ********");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		logger.info("**** Deleting User ********");
		
		Response respone = UserEndPoints2.deleteUser(this.userpayload.getUsername());
		Assert.assertEquals(respone.getStatusCode(), 200);
		
		logger.info("**** User is deleted ********");
	}
}
