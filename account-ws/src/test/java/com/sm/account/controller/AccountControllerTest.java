package com.sm.account.controller;

//@ActiveProfiles("test")
////@ExtendWith(SpringExtension.class)
////@WebMvcTest(controllers = AccountController.class)
//@SpringBootTest
////@AutoConfigureMockMvc
////@ContextConfiguration(classes = TestConfig.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
//@EnableAutoConfiguration(exclude = {ConfigClientAutoConfiguration.class})
//@Import(NativeRepositoryConfiguration.class)
class AccountControllerTest {

//    @MockBean
//    ConfigServerAutoConfiguration configServerAutoConfiguration;
//    @MockBean
//    private AccountService accountService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testFindAllAccountWithEmptyData() throws Exception {
//        List<AccountStatementDto> list = new ArrayList<>();
//        when(accountService.findAllAccountAndStatement(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(list);
//        mockMvc.perform(get("/account/statement")).andExpect(status().isOk()).andExpect(jsonPath("$.list").doesNotExist());
//    }
//
//    @Test
//    public void testFindAllAccountWithData() throws Exception {
//
//        List<AccountStatementDto> userAccountListStatementDto = createUserAccountListStatementDto();
//        when(accountService.findAllAccountAndStatement(null, null, null, null, null)).thenReturn(userAccountListStatementDto);
//        mockMvc.perform(get("/account/statement")).andExpect(status().isOk()).andExpect(jsonPath("$.list").exists()).andExpect(jsonPath("$.list.length()").value(1L));
//    }
//
//    public List<AccountStatementDto> createUserAccountListStatementDto() {
//        List<AccountStatementDto> accountStatementDtoList = new ArrayList<>();
//        AccountStatementDto accountStatementDto = new AccountStatementDto();
//        accountStatementDto.setAccountId(1L);
//        accountStatementDtoList.add(accountStatementDto);
//        accountStatementDto.setAccountType("current");
//        accountStatementDto.setId(1L);
//        accountStatementDto.setAccountNumber("SDF#S");
//        accountStatementDtoList.add(accountStatementDto);
//        return accountStatementDtoList;
//    }
//
//    @Test
//    public void testFindAccountWithEmptyData() throws Exception {
//        List<AccountStatementDto> list = new ArrayList<>();
//        when(accountService.findAccountAndStatementByUserId(anyString())).thenReturn(list);
//        mockMvc.perform(get("/account/123/statement")).andExpect(status().isOk()).andExpect(jsonPath("$.*.length()").isEmpty());
//    }
}

