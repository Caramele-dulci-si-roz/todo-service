package integration.ro.unibuc.hello.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import ro.unibuc.hello.exception.BadRequestException;
import ro.unibuc.hello.web.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	@InjectMocks
	GlobalExceptionHandler globalExceptionHandler;

	@Test
	void handleBadRequestException() {
		BadRequestException exception = new BadRequestException("TEST");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), globalExceptionHandler.handleBadRequestException(exception).getStatusCodeValue());
		Assertions.assertEquals("TEST", globalExceptionHandler.handleBadRequestException(exception).getBody());
	}

	@Test
	void bindException() {
		BindException exception = new BindException(new Object(), "test");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), globalExceptionHandler.bindException(exception).getStatusCodeValue());
	}
}