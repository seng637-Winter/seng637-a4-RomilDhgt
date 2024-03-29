**SENG 637 - Dependability and Reliability of Software Systems**

**Lab. Report \#4 – Mutation Testing and Web app testing**

| Group \#: 1    |     
| -------------- | 
| Student Names: |     
| Romil Dhagat   | 
| Yajur Vashisht |    
| Clark Harrison Dy |  
| Nick Nikolov |  
| Yene Irvine | 

# Introduction

In this lab, we explore Mutation Testing with Pitest and GUI Testing using Selenium and Sikulix. We evaluate test suite effectiveness by creating mutants of the System Under Test and running tests against them. Additionally, we automate UI test cases for website functionalities, ensuring coverage of various scenarios. We enhance our understanding of software testing concepts and gain practical skills for ensuring software reliability.

# Analysis of 10 Mutants of the Range class 

![alt text](<SS1.png>)

![alt text](<SS3.png>)

Analysis of at least 10 mutants produced by the Pitest for Range, and how they are killed or not by original test suite
As seen in the above screenshot, 51 survived, and 146 were killed. Below is a quick analysis on some of those mutants
* “90: Removed Conditional - replaced comparison check with false” - This mutant has survived, and most likely due to the fact that not a single test case in the test suite tests having an invalid constructor for Range. Upon skipping this conditional (due to check being replaced with false), an exception is not thrown, which was never expected to be thrown to begin with.
* “90: Removed Conditional - replaced comparison check with true” - Conversely, this mutant has been killed, since the test cases are not meant to throw an exception, and since it has, the mutant is killed accordingly.
* “95: Removed assignment to member variable lower” - This has been killed appropriately, since test cases in the test suite would be dealing with its default value (0.0 for a double, in this case) rather than its intended non-zero value, ultimately killing the mutant. However, this also comes with the fact that none of the test cases in the suite expects the value of lower in Range to be 0.0. A similar thing can be said about “96: Removed assignment to member variable upper”.
* “105: replaced double return with 0.0d for Range::getLowerBound” and “105: replaced double return with -(x + 1) for Range::getLowerBound” were both killed, since they directly affect the return of Range::getLowerBound(), which pretty much makes any test cases that rely on getting the lower bound value to fail, and thus the mutant is killed.
* “123: Decremented (a--) double field lower” and the other similar post-fix operation (e.g. a++) for both double field lower and double field upper have had its mutants survive. This is due to how the order of operations are executed, since the value of lower (or upper) in these mutants are returned before having its value decremented/incremented accordingly.
* “123: Decremented (--a) double field lower” and the other similar pre-fix operation (e.g. ++a) have, conversely, been killed appropriately due to the order of operations as described above. Since the values of lower (or upper) are decremented/incremented first before returning.
* “190: Removed conditional - replaced comparison check with false” and “190: Removed conditional - replaced comparison check with true” were also both killed. This is because our test cases cover both having our argument value be greater than or less than the upper bound. And so, replacing the check to be true or false would be caught by those test cases, and so the mutant is then killed accordingly.
* “193: Removed conditional - replaced comparison check with false” also goes through the same reasoning as changes to line 190. A test case for Range::constrain checks to see if the lower value is outside the range, and since the mutation replaces the check with false (instead of what would have been a true in this scenario), the mutated code is killed.
* “193: Removed conditional - replaced comparison check with true” - However, replacing the same comparison check with true does not yield the same result. The mutant in this case survived. This is likely due to the test cases provided making use of arguments that are either inside the range, above the range, or below the range. And so because of this, they either do not reach this conditional statement (due to it being an else-if), or when it does get reached, it is expected to be true anyways (in the case of the value being below the range’s lower bound).
* “218: replaced return value with null for Range::combine” - Due to the nature of the assignments, we did not create test cases for all functions. In this function, we unintentionally left a single test case for it, which tests combining two null values. Since the values are already null to begin with in our one and only test case, the return expected is null, and so the mutant changing the return to be null survives.


# Report all the statistics and the mutation score for each test class

![alt text](<SS2.png>)

![alt text](<SS4.png>)

# Analysis drawn on the effectiveness of each of the test classes

### Range Test Classes 

Although high test coverage does not necessarily guarantee effective mutation testing, low test coverage means that we are not effectively testing certain parts of the code thoroughly. As we can see throughout our mutation testing, since the test classes (e.g. RangeTest) only covers a small amount of the functions in Range.java, our mutation coverage percentage is pretty low as well (e.g. 12%), which indicates just how ineffective our test class is in detecting changes in the code. Additionally, by improving the test cases iteratively, checking the results of the mutation testing, analyzing why/how a mutant survived, and by making improvements to the test cases, making sure we are able to cover situations such as edge cases, we’re able to adequately test the code, and improve the effectiveness of our test class.

### Data Utilities Test Classes 

The DataUtilitiesTest test class also demonstrated relatively ineffective mutation testing initially. At the beginning of this assignment, certain methods within DataUtilities, such as calculateColumnTotal and the clone method, lacked comprehensive test coverage, potentially leaving room for undetected bugs or unexpected behavior. However, through the targeted addition of test cases—aiming to cover positive, negative, and null scenarios, as well as handling edge cases—there was a notable improvement in the mutation coverage. This indicates that a wider variety of possible bugs are now being caught and tested against, thus enhancing the robustness of the code. The enhancement in mutation coverage not only improves confidence in the correctness of the DataUtilities class but also highlights the effectiveness of using mutation testing as a strategy to identify weaknesses in test suites.

# A discussion on the effect of equivalent mutants on mutation score accuracy

In mutation testing, an equivalent mutant is one that, despite the syntactic change, does not result in any change to the program's execution behavior from the user's perspective. 

### Range:

The following is an analysis of some of the surviving mutants within Range.java in terms of their potential eligibility as equivalent mutants.

Some selected snippets of Range.java following PITest are shown below. The red highlighted lines indicate at least one mutant has survived that line, whereas the green highlighted lines indicate that all mutants of that line have been killed. 

![alt text](<SS9.png>)

Within this segment, there are some potential equivalent mutants among the survived mutants identified:

LINE 95:
Incremented (a++) double local variable number 1 → SURVIVED
Decremented (a--) double local variable number 1 → SURVIVED

If these increments or decrements occur after the last actual use of the lower or upper variables in the constructor, they could be equivalent mutants. This is because altering the value of a variable after its last consequential use does not change the externally observable behavior of the class.

LINE 96:
Less or equal to less than → SURVIVED

If this mutation does not change the logical flow of the program, it could be considered an equivalent mutant. However, this is context-dependent and would require further analysis of how Range objects are created and used in the program.

Another segment analyzed within the Range.java class is the public boolean contains(double value) method:

![alt text](<SS10.png>)

Among the survived mutants found in this code segment, none represent a realistic equivalent mutant. An explanation of why some of these survived mutants do not qualify as such as is below:

LINE 162:
changed conditional boundary → SURVIVED
Any change in the conditional boundary alters the edge case behavior where value is exactly equal to this.upper. Such a mutation would not preserve the original method behavior when value == this.upper, which should return true but the mutated code may return false. Since the behavior changes for this specific case, it is not an equivalent mutant.

LINE 165:
Removed conditional - replaced comparison check with true → SURVIVED
This mutation suggests that the final return statement has been altered to always return true, regardless of the actual comparison between value, this.lower, and this.upper.
Given the context of the method, if this mutation is making the method always return true, then it is not an equivalent mutant. This is because the method’s intended functionality is to accurately report whether a given value is within the range or not. 

### DataUtilities.java:

The following is an analysis of some of the potentially equivalent mutants among those that have survived in the calculateColumnTotal method within DataUtilities.java. A view of the function following PITest is shown below. The red highlighted lines indicate at least one mutant has survived that line, whereas the green highlighted lines indicate that all mutants of that line have been killed.

![alt text](<SS11.png>)

The following is an analysis of some of the survived mutants that could potentially be classified as equivalent mutants:

LINE 130: "Incremented (a++) double local variable number 2" → SURVIVED:
If the mutation increments a local variable that's used to accumulate a sum after it's read, and before it’s written to again, the mutation could be equivalent as the final value of total would remain unchanged. However, since this mutation occurs within the loop, it could result in a different sum, indicating that it also may not be an equivalent mutant.

LINE 133: "Substituted 0 with -1" → SURVIVED:
Substituting 0 with -1 in a loop condition check could make the loop non-executable if the check was previously (r2 < 0), which would be an equivalent mutation if r2 was never negative. However, this line seems to contain an anomaly as the loop condition r2 > rowCount would never be true if r2 starts at 0. This loop seems to be an error or dead code and the mutation here might be highlighting a flaw in the code logic.

LINE 133: Incremented/decremented local integer variables (number 4 and 5) → SURVIVED:
These mutations suggest that local loop variables r2 (assuming they are r2 as there are no other integer variables in that scope) are being incremented or decremented. In the context of the provided method, the second loop with the variable r2 seems incorrect as it will never execute (r2 > rowCount will always be false when r2 starts at 0). Therefore, any mutations to r2 would not impact the method's behavior, since the loop does not run at all. These might be considered equivalent mutants because whether r2 is incremented or decremented has no effect on the outcome due to the faulty loop condition.

In summary, an automatic way to detect equivalent mutants would involve analyzing the impact of each mutation in the context of how variables are used throughout the method, considering factors like control flow, the range of valid inputs, and the possible states of the program. The benefit of this approach is a deeper understanding of the code and test cases. The disadvantage is that it can be complex and time-consuming. The assumption is that the code logic and its requirements are well-understood.

To automatically detect equivalent mutants, a potential process would be:

Analyze code paths to ensure that the mutated line is reachable.
Use symbolic execution to determine if the mutation changes the outcome.
Cross-reference the mutant with existing unit tests to check if it affects assertions.

This process was applied in an effort to identify equivalent mutants among the survived mutants.

# A discussion of what could have been done to improve the mutation score of the test suites

### Range 

* Initially, we have a mutation coverage of 12% (146/1259), which is largely in part of the fact that we do not have test cases for all functions in Range. A quick fix to increasing this score is by simply adding more test cases that cover more functions. Additionally, we have an initial test strength of 74.11% (146/197 mutants killed).
* One way to kill more mutants is to add more test cases (e.g. for Range::constrain), since there is only a limited number of test cases. By having a higher code coverage, we increase the likelihood of detecting mutations that affect different parts of the function. An example of a test case that can be made is to change the below range test to a negative value, which can kill mutations where the lower bound is negated (and becomes a potentially large positive value).
* Additionally, we can create more test cases for various functions, such as Range::expandToInclude() and Range::intersects(). By having more test cases test different functions in the class, we are able to increase our mutation score.
* Another would be adding more test cases that handle various, more diverse range of inputs, as well as adding test cases with appropriate error handling. This would help expose vulnerabilities, as well as error handling of invalid inputs or edge cases. An example is for when calling the constructor for Range. When lower > upper, that should throw an error, so a test case for that should be added.
* There is also the Range::combine() function, which we can include more test cases, such as combining valid ranges, and combining ranges where either of the range is null. Designing this test case is a little tricky, since I plan on using Range::equals() to verify that the test cases are passing JUnit testing, which would introduce more mutants. Normally, this would not be an issue since we would ideally have test cases for all functions, but since that is not the case for this assignment, I can either add more test cases specifically for Range::equals(), or a workaround is using Range::getLowerBound() and Range::getUpperBound() since those are functions already used in our test cases. In our case, we opted to use Range::equals(), and added additional test cases for those as well.
* With the changes above, we are able to kill more mutants (356 vs 146), and achieve a 16% better mutation score (28% vs 12%), while also maintaining a relatively close test strength from prior to adding more test cases (73% vs 74%).


### Data Utilites 

The initial mutation coverage for DataUtilities.java was 39% (345/881 mutants), indicating the effectiveness of our current test suite. The initial test strength was 84%, with 345 mutants killed out of a total of 409.

It was noted that particular functions, such as equal(double[][] a, double[][] b), clone(double[][] source) and calculateColumnTotal(Values2D data, int column, int[] validRows), that lacked comprehensive test cases. The mutation coverage could be improved by including test suites for functions like these. 

In an effort to increase the mutation coverage by at least 10%, three test cases were implemented for each of these functions, for a total of nine test cases. They are described and then implemented below:

(1) calculateColumnTotal: 
* Sum Positive Values in a Column: This test verifies that calculateColumnTotal correctly sums up positive values in a specified column.
* Handle Null Values in a Column: This test ensures that calculateColumnTotal correctly handles null values by ignoring them and not throwing an exception.
* Sum Negative Values in a Column: This test checks if calculateColumnTotal can sum up negative values correctly.

![alt text](<SS5.png>)
![alt text](<SS6.png>)

(2) Equal: 
* Handle equal arrays: This test verifies that ‘equal’ returns ‘true’ for two identical arrays
* Handle unequal arrays: This test verifies that ‘equal’ returns ‘false’ for arrays that differ in content or length
* Handle one null array: This test verifies that ‘equal’ returns ‘false’ if one array is null and the other is non-null 

![alt text](<SS7.png>)

(3) Clone
* Clone Non-Empty Array: This test verifies that the clone method accurately clones a non-empty array, producing a new array instance with the same content as the source array.
* Clone Array with Null Entries: This test verifies that the clone method can handle arrays containing null entries without throwing an exception, correctly cloning the source array including its null entries.
* Clone Empty Array: This test checks if the clone method can clone an empty array, resulting in a new array instance that is also empty, demonstrating the method's ability to handle arrays with no elements.

![alt text](<SS8.png>)

The implementation of these 9 test cases resulted in a significant improvement across multiple metrics pertaining to DataUtilities.java’s Pit Test Coverage. Most notably, the mutation coverage increased from 39% (345/881) to 53% (470/881), a rise of 14%. The line coverage also increased from 47% to 64%, an increase of 17%. The Test Strength also improved marginally from 84% to 85%. 

This significant improvement highlights the importance of ensuring adequate coverage in terms of test cases and the functions they are being applied to.  

# Why do we need mutation testing? Advantages and disadvantages of mutation testing

Mutation testing serves as a valuable technique in software testing for several reasons. Firstly, it increases the robustness of test suites by measuring their ability to detect faults in the code. By introducing small, intentional mutations to the codebase, mutation testing evaluates the effectiveness of tests in identifying these alterations, thus providing insight into the quality of the testing process. Secondly, it identifies weak spots i ntest suites, so that developers to improve test coverage and design more better tests.

However, mutation testing also comes with its set of limitations. One significant drawback is its computational cost and time-consuming nature, especially for large codebases or complex systems. Running mutation tests can be resource-intensive, requiring substantial processing power and time to generate and execute mutants. Another drawback is that it might not even find all the issues in the software. Also if the test cases are not adequately designed or lack sufficient coverage, mutation testing may yield misleading results.

# Explain your SELENUIM test case design process

Selenium is a tool used for automating GUI testing of a web application. Selenium is used to record the testing procedure so it can be repeated for each iteration of the web application. The website being tested for this project is https://www.homedepot.ca/en/home.html. 

### Home Depot Website - Test Cases

Cart 

| Test Case             | Description                                                                        | Inputs                                                                                            | Expected Outputs       | Pass / Fail |
|-----------------------|------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|------------------------|-------------|
| Add_to_Cart           | The correct Item is successfully added to the cart                                 | {Model: 1125STSN48}                                                                               | {Model: 1125STSN4}     | Pass        |
| Cart_Total_One_Item   | Asserts the cart total price equals the single cart item                           | {Item:Veranda 32-inch x 80-inch x 4-9/16-inch 6 Panel Single Primed Steel Prehung Front Door LH}  | {Total Price: $359.10} | Pass        |
| Cart_Total_Zero_Items | Asserts the cart total price to be $0 while no items are in the cart               | {}                                                                                                | {0 Items}              | Pass        |
| Cart_Total_Multiple   | Asserts the cart total price equals the sum of all items in the cart               | {Item: Model: 249530 Model: 2035892  Qty: 1}                                                      | {Price: $1676.80}      | Pass        |
| Cart_Total_Duplicate  | Asserts the cart total price equals the sum of the item multiplied by its quantity | {Item: Model: 17197956  Qty: 2}                                                                   | {Price: $4361.70}      | Pass        |

Settings

| Test Case             | Description                                     | Inputs                                                    | Expected Outputs                                        | Pass / Fail |
|-----------------------|-------------------------------------------------|-----------------------------------------------------------|---------------------------------------------------------|-------------|
| English_To_French     | User changes the website from English to French | {English to French                                        | Manager by Store  Translates To   “Magasiner par rayon” | Pass        |
| French_To_English     | User changes the website from French to English | {French to English                                        | “Magasiner par rayon” Translates to  Manager by Store   | Pass        |
| Change_Store_Location | User changes their local store location         | {Calgary Northeast Marlborough Mall to Calgary North Hill | My Store: Calgary North Hill                            | Pass        |

Special Offers section

| Test Case                  | Description                                                    | Inputs                                           | Expected Outputs      | Pass / Fail |
|----------------------------|----------------------------------------------------------------|--------------------------------------------------|-----------------------|-------------|
| Discount_And_Regular_Price | USer selects one regularly priced item and one discounted item | {“Model: 3469528.020 And  Model: 1125STSN48“}    | {“You saved $32.00”}  | Pass        |
| Multiple_Discount          | User adds multiple discounted items to cart                    | {“Model: 3469528.020 And  Model: 3380516ST.020“} | {“You saved $103.00”} |             |
| Discount_Price             | User selects one discounted item                               | {“Model: 3469528.020“}                           | {“You saved $32.00”}  | Pass        |

Tool Rental

| Test Case        | Description                                                                                         | Inputs                                                                                    | Expected Outputs                                                                                                            | Pass / Fail |
|------------------|-----------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|-------------|
| Available_Tool   | Search for a tool rental that is available at the current location at the current time of testing   | {Mechanical Earth Drill at Calgary Northeast Marlborough Mall Location}                   | {“Available to Rent at CALGARY NORTHEAST (MARLBOROUGH) #7061 Please call store or reserve online to confirm availability.”} | Pass        |
| Unavailable_Tool | Search for a tool rental that is unavailable at the current location at the current time of testing | {Bed Shaper at Calgary Northeast Marlborough Mall Location}                               | {“Unavailable to Rent at CALGARY NORTHEAST (MARLBOROUGH) #7061 Please try another location.”}                               | Pass        |
| Rental_Price     | User checks the rental price of an item                                                             | {MAX Fuel Lithium-Ion Cordless Handheld Core Drill Kit with Stand - 4 week rental price”} | {“$540.00”}                                                                                                                 | Pass        |

Weekly Offer Section

| Test Case                   | Description                                                       | Inputs                                     | Expected Outputs   | Pass / Fail |
|-----------------------------|-------------------------------------------------------------------|--------------------------------------------|--------------------|-------------|
| Price_ Continuity           | Price quoted in the flyer must match the actual price of the item | {WD-40 Original Formula, Smart Straw 325g} | {“$8.98”}          | Pass        |
| Vinyl_Flooring_Weekly_Offer | User clicks on the Vinyl flooring item in the weekly flyer        | {Vinyl Flooring Item through weekly flyer} | {$1.84 per sq. ft} | FAIL        |

### Shop Smart Canada Website - Test Cases

Sign up

| Test Case                          | Description                                                 | Inputs                                                              | Expected Outputs                              | Pass / Fail |
|------------------------------------|-------------------------------------------------------------|---------------------------------------------------------------------|-----------------------------------------------|-------------|
| Basic_Sign_up                      | Sign up using the user information with a valid password    | {yajurvashisht@gmail.com, test, Yajur, Vashisht, (780) 919-8100, …} | { User account created successfully }         | Pass        |
| Incorrect_Password_Format_ Sign_up | Sign up using the user information with an invalid password | {yajurvashisht@gmail.com, test, Yajur, Vashisht, (780) 919-8100, …} | { User account was not created successfully } | Pass        |

Login

| Test Case                  | Description                          | Inputs                                       | Expected Outputs                                   | Pass / Fail |
|----------------------------|--------------------------------------|----------------------------------------------|----------------------------------------------------|-------------|
| Login_With_ Correct_Info   | Login user using valid credentials   | {yajurvashisht@gmail.com, test}              | {Welcome to your dashboard, user login successful} | Pass        |
| Login_With_ Incorrect_Info | Login user using invalid credentials | {yajurvashisht@gmail.com, not-test-password} | {Your email address or password is incorrect}      | Pass        |

Cart

| Test Case              | Description                                | Inputs                                                          | Expected Outputs | Pass / Fail |
|------------------------|--------------------------------------------|-----------------------------------------------------------------|------------------|-------------|
| Add_to_Cart            | Add a single item to the cart              | {1332249}                                                       | {$1,517.64}      | Pass        |
| Delete_from_ Cart      | Delete a single item from the cart         | {8777554}                                                       | {$498.88}        | Pass        |
| Duplicates_in_ Cart    | Add duplicates of one item into the cart   | {1502920, 1502920, 1502920, 1502920, 1502920, 1502920, 1502920} | {$5,188.14}      | Pass        |
| Multiple_Items_in_Cart | Add multiple different items into the cart | {318145, 46611, 47850}                                          | {$1,343.97}      | Pass        |

Settings

| Test Case        | Description                                | Inputs                       | Expected Outputs                          | Pass / Fail |
|------------------|--------------------------------------------|------------------------------|-------------------------------------------|-------------|
| Change_Name      | Update the name under the user account     | {SENG, 637}                  | {Your account details have been updated.} | Pass        |
| Change_Email     | Update the email under the user account    | {yajur.vashisht@ucalgary.ca} | {Your account details have been updated.} | Pass        |
| Add_Company      | Add a company to the user account          | {University of Calgary}      | {Your account details have been updated.} | Pass        |
| Change_ Password | Update the password under the user account | {notlast password1}          | {Your account details have been updated.} | Pass        |

Promotions

| Test Case                   | Description                                        | Inputs          | Expected Outputs                                          | Pass / Fail |
|-----------------------------|----------------------------------------------------|-----------------|-----------------------------------------------------------|-------------|
| Incorrect_ Discount_Code    | Enter an incorrect discount code for items in cart | {DISCOUNT_FAKE} | {The coupon code DISCOUNT_FAKE you entered is not valid.} | Pass        |
| Free_Shipping_Item_to_ Cart | Add an item with free shipping  into the cart      | {315179}        | {$316.22}                                                 | Pass        |

# Explain the use of assertions and checkpoints

Assertions are used in selenium to verify the application is performing as expected. In this case, an assertion was added to the “Cart_Total” testing case; which specifies that the total price of the cart must be equal to the sum of all items in the cart. If this test fails, then that is an indication the summation is not working for the items in the cart. 

# how did you test each functionaity with different test data

For testing the functionality with different test data, our Team created distinct test cases tailored to cover a broad range of scenarios, including both standard operations and edge cases. Using Selenium, we recorded the test sequences, entering various sets of inputs directly into the web interface and then recording the website’s behavior. This hands-on approach allowed us to closely observe and verify the application under different conditions. Although manual testing does not ensure a thorough evaluation, we were able to grasp functional robustness and user experience. 

# How the team work/effort was divided and managed

The team's work and efforts were divided based on the complexity and nature of various aspects of the assignments. We used a buddy system for the assignment while splitting up the test cases done in Selenium. This approach allowed all of the group to learn aspects of testing while being able to help other group members.

# Difficulties encountered, challenges overcome, and lessons learned

One selenium test kept failing an assertion even though the correct text was displayed. We had to reduce the playback speed of the test to give the browser enough time after the value was updated and the assertion. The assertion was being checked before the updated cart total was able to be calculated. The HomeDepot website blocked certain functions from Selenium. Such as search bar and login, it did not allow the use of these features for automatic testing

# Comments/feedback on the assignment itself

The assignment taught us practical exploration of automated testing, spotlighting the synergy between manual and automated methods. The Mutation testing section highlighted the critical need for comprehensive test coverage to identify subtle, yet impactful, code defects.