//------------------------------------------------------------------------------------------------------------
//Пользователи и роли
insert into users (username,name,hashed_password,profile_picture) values ('user','Юзвер','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe',lo_from_bytea(0, '\xffd8ffe000104a46494600010101004800480000ffe20c584943435f50524f46494c4500010100000c484c696e6f021000006d6e74725247422058595a2007ce00020009000600310000616373704d5346540000000049454320735247420000000000000000000000000000f6d6000100000000d32d4850202000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001163707274000001500000003364657363000001840000006c77747074000001f000000014626b707400000204000000147258595a00000218000000146758595a0000022c000000146258595a0000024000000014646d6e640000025400000070646d6464000002c400000088767565640000034c0000008676696577000003d4000000246c756d69000003f8000000146d6561730000040c0000002474656368000004300000000c725452430000043c0000080c675452430000043c0000080c625452430000043c0000080c7465787400000000436f70797269676874202863292031393938204865776c6574742d5061636b61726420436f6d70616e790000646573630000000000000012735247422049454336313936362d322e31000000000000000000000012735247422049454336313936362d322e31000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000058595a20000000000000f35100010000000116cc58595a200000000000000000000000000000000058595a200000000000006fa2000038f50000039058595a2000000000000062990000b785000018da58595a2000000000000024a000000f840000b6cf64657363000000000000001649454320687474703a2f2f7777772e6965632e636800000000000000000000001649454320687474703a2f2f7777772e6965632e63680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000064657363000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d207352474200000000000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d20735247420000000000000000000000000000000000000000000064657363000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e3100000000000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e31000000000000000000000000000000000000000000000000000076696577000000000013a4fe00145f2e0010cf140003edcc0004130b00035c9e0000000158595a2000000000004c09560050000000571fe76d6561730000000000000001000000000000000000000000000000000000028f0000000273696720000000004352542063757276000000000000040000000005000a000f00140019001e00230028002d00320037003b00400045004a004f00540059005e00630068006d00720077007c00810086008b00900095009a009f00a400a900ae00b200b700bc00c100c600cb00d000d500db00e000e500eb00f000f600fb01010107010d01130119011f0125012b01320138013e0145014c0152015901600167016e0175017c0183018b0192019a01a101a901b101b901c101c901d101d901e101e901f201fa0203020c0214021d0226022f02380241024b0254025d02670271027a0284028e029802a202ac02b602c102cb02d502e002eb02f50300030b03160321032d03380343034f035a03660372037e038a039603a203ae03ba03c703d303e003ec03f9040604130420042d043b0448045504630471047e048c049a04a804b604c404d304e104f004fe050d051c052b053a05490558056705770586059605a605b505c505d505e505f6060606160627063706480659066a067b068c069d06af06c006d106e306f507070719072b073d074f076107740786079907ac07bf07d207e507f8080b081f08320846085a086e0882089608aa08be08d208e708fb09100925093a094f09640979098f09a409ba09cf09e509fb0a110a270a3d0a540a6a0a810a980aae0ac50adc0af30b0b0b220b390b510b690b800b980bb00bc80be10bf90c120c2a0c430c5c0c750c8e0ca70cc00cd90cf30d0d0d260d400d5a0d740d8e0da90dc30dde0df80e130e2e0e490e640e7f0e9b0eb60ed20eee0f090f250f410f5e0f7a0f960fb30fcf0fec1009102610431061107e109b10b910d710f511131131114f116d118c11aa11c911e81207122612451264128412a312c312e31303132313431363138313a413c513e5140614271449146a148b14ad14ce14f01512153415561578159b15bd15e0160316261649166c168f16b216d616fa171d17411765178917ae17d217f7181b18401865188a18af18d518fa19201945196b199119b719dd1a041a2a1a511a771a9e1ac51aec1b141b3b1b631b8a1bb21bda1c021c2a1c521c7b1ca31ccc1cf51d1e1d471d701d991dc31dec1e161e401e6a1e941ebe1ee91f131f3e1f691f941fbf1fea20152041206c209820c420f0211c2148217521a121ce21fb22272255228222af22dd230a23382366239423c223f0241f244d247c24ab24da250925382568259725c725f726272657268726b726e827182749277a27ab27dc280d283f287128a228d429062938296b299d29d02a022a352a682a9b2acf2b022b362b692b9d2bd12c052c392c6e2ca22cd72d0c2d412d762dab2de12e162e4c2e822eb72eee2f242f5a2f912fc72ffe3035306c30a430db3112314a318231ba31f2322a3263329b32d4330d3346337f33b833f1342b3465349e34d83513354d358735c235fd3637367236ae36e937243760379c37d738143850388c38c839053942397f39bc39f93a363a743ab23aef3b2d3b6b3baa3be83c273c653ca43ce33d223d613da13de03e203e603ea03ee03f213f613fa23fe24023406440a640e74129416a41ac41ee4230427242b542f7433a437d43c044034447448a44ce45124555459a45de4622466746ab46f04735477b47c04805484b489148d7491d496349a949f04a374a7d4ac44b0c4b534b9a4be24c2a4c724cba4d024d4a4d934ddc4e254e6e4eb74f004f494f934fdd5027507150bb51065150519b51e65231527c52c75313535f53aa53f65442548f54db5528557555c2560f565c56a956f75744579257e0582f587d58cb591a596959b85a075a565aa65af55b455b955be55c355c865cd65d275d785dc95e1a5e6c5ebd5f0f5f615fb36005605760aa60fc614f61a261f56249629c62f06343639763eb6440649464e9653d659265e7663d669266e8673d679367e9683f689668ec6943699a69f16a486a9f6af76b4f6ba76bff6c576caf6d086d606db96e126e6b6ec46f1e6f786fd1702b708670e0713a719571f0724b72a67301735d73b87414747074cc7528758575e1763e769b76f8775677b37811786e78cc792a798979e77a467aa57b047b637bc27c217c817ce17d417da17e017e627ec27f237f847fe5804780a8810a816b81cd8230829282f4835783ba841d848084e3854785ab860e867286d7873b879f8804886988ce8933899989fe8a648aca8b308b968bfc8c638cca8d318d988dff8e668ece8f368f9e9006906e90d6913f91a89211927a92e3934d93b69420948a94f4955f95c99634969f970a977597e0984c98b89924999099fc9a689ad59b429baf9c1c9c899cf79d649dd29e409eae9f1d9f8b9ffaa069a0d8a147a1b6a226a296a306a376a3e6a456a4c7a538a5a9a61aa68ba6fda76ea7e0a852a8c4a937a9a9aa1caa8fab02ab75abe9ac5cacd0ad44adb8ae2daea1af16af8bb000b075b0eab160b1d6b24bb2c2b338b3aeb425b49cb513b58ab601b679b6f0b768b7e0b859b8d1b94ab9c2ba3bbab5bb2ebba7bc21bc9bbd15bd8fbe0abe84beffbf7abff5c070c0ecc167c1e3c25fc2dbc358c3d4c451c4cec54bc5c8c646c6c3c741c7bfc83dc8bcc93ac9b9ca38cab7cb36cbb6cc35ccb5cd35cdb5ce36ceb6cf37cfb8d039d0bad13cd1bed23fd2c1d344d3c6d449d4cbd54ed5d1d655d6d8d75cd7e0d864d8e8d96cd9f1da76dafbdb80dc05dc8add10dd96de1cdea2df29dfafe036e0bde144e1cce253e2dbe363e3ebe473e4fce584e60de696e71fe7a9e832e8bce946e9d0ea5beae5eb70ebfbec86ed11ed9cee28eeb4ef40efccf058f0e5f172f1fff28cf319f3a7f434f4c2f550f5def66df6fbf78af819f8a8f938f9c7fa57fae7fb77fc07fc98fd29fdbafe4bfedcff6dffffffdb004300090606080605090807080a09090a0d160e0d0c0c0d1a131410161f1c21201f1c1e1e2327322a23252f251e1e2b3b2c2f3335383838212a3d413c364132373835ffdb004301090a0a0d0b0d190e0e1935241e243535353535353535353535353535353535353535353535353535353535353535353535353535353535353535353535353535ffc0001108001a001a03012200021101031101ffc4001a000002020300000000000000000000000005060107030408ffc4002e10000103020404030901000000000000000102030400110506122113314161075171151623243234425263d1ffc40017010101010100000000000000000000000001030002ffc4001a110003000301000000000000000000000000010203122131ffda000c03010002110311003f00796d8bf4a0f27376090a7ae2bf21c41697c371ee02cb28579172da47973b5e995b481b9e94baec44c9c36630f14f001712b6cb62ca049ff68c95a2298637e0614ca4a2e2c41dc11c8d63e0d46070bd9d97e1425385d547610d6b50dd5615b5a3b5764d8b39c33fc7cb0fa61351d52e6adbd653ab4a1b04eda8f7b1e40d2546f112ecc97a74171f9ab376d285da3a7b904f4f3b1268467d5139f315b927e327aff3450547d07d6b54aaf422dcf516165df1516db8db38fb2d9413a4cb676d3dd483d3d0d59a14149052a4949dc1bd736382e8503cad56b6013247bb9877cc3bf6ad7e67f414a03fffd9'));
insert into users (username,name,hashed_password,profile_picture) values ('admin','Админ','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.',lo_from_bytea(0, '\xffd8ffe000104a46494600010101004800480000ffe20c584943435f50524f46494c4500010100000c484c696e6f021000006d6e74725247422058595a2007ce00020009000600310000616373704d5346540000000049454320735247420000000000000000000000000000f6d6000100000000d32d4850202000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001163707274000001500000003364657363000001840000006c77747074000001f000000014626b707400000204000000147258595a00000218000000146758595a0000022c000000146258595a0000024000000014646d6e640000025400000070646d6464000002c400000088767565640000034c0000008676696577000003d4000000246c756d69000003f8000000146d6561730000040c0000002474656368000004300000000c725452430000043c0000080c675452430000043c0000080c625452430000043c0000080c7465787400000000436f70797269676874202863292031393938204865776c6574742d5061636b61726420436f6d70616e790000646573630000000000000012735247422049454336313936362d322e31000000000000000000000012735247422049454336313936362d322e31000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000058595a20000000000000f35100010000000116cc58595a200000000000000000000000000000000058595a200000000000006fa2000038f50000039058595a2000000000000062990000b785000018da58595a2000000000000024a000000f840000b6cf64657363000000000000001649454320687474703a2f2f7777772e6965632e636800000000000000000000001649454320687474703a2f2f7777772e6965632e63680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000064657363000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d207352474200000000000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d20735247420000000000000000000000000000000000000000000064657363000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e3100000000000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e31000000000000000000000000000000000000000000000000000076696577000000000013a4fe00145f2e0010cf140003edcc0004130b00035c9e0000000158595a2000000000004c09560050000000571fe76d6561730000000000000001000000000000000000000000000000000000028f0000000273696720000000004352542063757276000000000000040000000005000a000f00140019001e00230028002d00320037003b00400045004a004f00540059005e00630068006d00720077007c00810086008b00900095009a009f00a400a900ae00b200b700bc00c100c600cb00d000d500db00e000e500eb00f000f600fb01010107010d01130119011f0125012b01320138013e0145014c0152015901600167016e0175017c0183018b0192019a01a101a901b101b901c101c901d101d901e101e901f201fa0203020c0214021d0226022f02380241024b0254025d02670271027a0284028e029802a202ac02b602c102cb02d502e002eb02f50300030b03160321032d03380343034f035a03660372037e038a039603a203ae03ba03c703d303e003ec03f9040604130420042d043b0448045504630471047e048c049a04a804b604c404d304e104f004fe050d051c052b053a05490558056705770586059605a605b505c505d505e505f6060606160627063706480659066a067b068c069d06af06c006d106e306f507070719072b073d074f076107740786079907ac07bf07d207e507f8080b081f08320846085a086e0882089608aa08be08d208e708fb09100925093a094f09640979098f09a409ba09cf09e509fb0a110a270a3d0a540a6a0a810a980aae0ac50adc0af30b0b0b220b390b510b690b800b980bb00bc80be10bf90c120c2a0c430c5c0c750c8e0ca70cc00cd90cf30d0d0d260d400d5a0d740d8e0da90dc30dde0df80e130e2e0e490e640e7f0e9b0eb60ed20eee0f090f250f410f5e0f7a0f960fb30fcf0fec1009102610431061107e109b10b910d710f511131131114f116d118c11aa11c911e81207122612451264128412a312c312e31303132313431363138313a413c513e5140614271449146a148b14ad14ce14f01512153415561578159b15bd15e0160316261649166c168f16b216d616fa171d17411765178917ae17d217f7181b18401865188a18af18d518fa19201945196b199119b719dd1a041a2a1a511a771a9e1ac51aec1b141b3b1b631b8a1bb21bda1c021c2a1c521c7b1ca31ccc1cf51d1e1d471d701d991dc31dec1e161e401e6a1e941ebe1ee91f131f3e1f691f941fbf1fea20152041206c209820c420f0211c2148217521a121ce21fb22272255228222af22dd230a23382366239423c223f0241f244d247c24ab24da250925382568259725c725f726272657268726b726e827182749277a27ab27dc280d283f287128a228d429062938296b299d29d02a022a352a682a9b2acf2b022b362b692b9d2bd12c052c392c6e2ca22cd72d0c2d412d762dab2de12e162e4c2e822eb72eee2f242f5a2f912fc72ffe3035306c30a430db3112314a318231ba31f2322a3263329b32d4330d3346337f33b833f1342b3465349e34d83513354d358735c235fd3637367236ae36e937243760379c37d738143850388c38c839053942397f39bc39f93a363a743ab23aef3b2d3b6b3baa3be83c273c653ca43ce33d223d613da13de03e203e603ea03ee03f213f613fa23fe24023406440a640e74129416a41ac41ee4230427242b542f7433a437d43c044034447448a44ce45124555459a45de4622466746ab46f04735477b47c04805484b489148d7491d496349a949f04a374a7d4ac44b0c4b534b9a4be24c2a4c724cba4d024d4a4d934ddc4e254e6e4eb74f004f494f934fdd5027507150bb51065150519b51e65231527c52c75313535f53aa53f65442548f54db5528557555c2560f565c56a956f75744579257e0582f587d58cb591a596959b85a075a565aa65af55b455b955be55c355c865cd65d275d785dc95e1a5e6c5ebd5f0f5f615fb36005605760aa60fc614f61a261f56249629c62f06343639763eb6440649464e9653d659265e7663d669266e8673d679367e9683f689668ec6943699a69f16a486a9f6af76b4f6ba76bff6c576caf6d086d606db96e126e6b6ec46f1e6f786fd1702b708670e0713a719571f0724b72a67301735d73b87414747074cc7528758575e1763e769b76f8775677b37811786e78cc792a798979e77a467aa57b047b637bc27c217c817ce17d417da17e017e627ec27f237f847fe5804780a8810a816b81cd8230829282f4835783ba841d848084e3854785ab860e867286d7873b879f8804886988ce8933899989fe8a648aca8b308b968bfc8c638cca8d318d988dff8e668ece8f368f9e9006906e90d6913f91a89211927a92e3934d93b69420948a94f4955f95c99634969f970a977597e0984c98b89924999099fc9a689ad59b429baf9c1c9c899cf79d649dd29e409eae9f1d9f8b9ffaa069a0d8a147a1b6a226a296a306a376a3e6a456a4c7a538a5a9a61aa68ba6fda76ea7e0a852a8c4a937a9a9aa1caa8fab02ab75abe9ac5cacd0ad44adb8ae2daea1af16af8bb000b075b0eab160b1d6b24bb2c2b338b3aeb425b49cb513b58ab601b679b6f0b768b7e0b859b8d1b94ab9c2ba3bbab5bb2ebba7bc21bc9bbd15bd8fbe0abe84beffbf7abff5c070c0ecc167c1e3c25fc2dbc358c3d4c451c4cec54bc5c8c646c6c3c741c7bfc83dc8bcc93ac9b9ca38cab7cb36cbb6cc35ccb5cd35cdb5ce36ceb6cf37cfb8d039d0bad13cd1bed23fd2c1d344d3c6d449d4cbd54ed5d1d655d6d8d75cd7e0d864d8e8d96cd9f1da76dafbdb80dc05dc8add10dd96de1cdea2df29dfafe036e0bde144e1cce253e2dbe363e3ebe473e4fce584e60de696e71fe7a9e832e8bce946e9d0ea5beae5eb70ebfbec86ed11ed9cee28eeb4ef40efccf058f0e5f172f1fff28cf319f3a7f434f4c2f550f5def66df6fbf78af819f8a8f938f9c7fa57fae7fb77fc07fc98fd29fdbafe4bfedcff6dffffffdb004300090606080605090807080a09090a0d160e0d0c0c0d1a131410161f1c21201f1c1e1e2327322a23252f251e1e2b3b2c2f3335383838212a3d413c364132373835ffdb004301090a0a0d0b0d190e0e1935241e243535353535353535353535353535353535353535353535353535353535353535353535353535353535353535353535353535ffc0001108001a001a03012200021101031101ffc400190000020301000000000000000000000000040500020706ffc4002b100001030302050109000000000000000001020311000405061213213141517314152233376182a2b1ffc400160101010100000000000000000000000000020103ffc4001a110003010101010000000000000000000000010221031112ffda000c03010002110311003f00ccd02051d7f84c958636defae2dcb76f7090b6d532483d2476a0c2429241e7222b43ba2de574f58a5e7dd71a36282d927e127985a48fb181e68f4af9487ce15b68ce1077a4cf51536d428e15c2900c804a41ec62ad14d699bc1f6274c3d91c35c655e743166cee4a206e5bcb1d93e04f2934d34d241d0d93f783894b4d3db994adcd813b809dbf9f9eb4ef1ff4cf17e8b47f6a49af9b43790b1421094a38cb3b408131d6b57cd35a49b72f0e598c7a9ec81b70a0a093f311cc19ee0d303a695b8c5d263d334669648f666390ea3fa69dc0f15661781aa7e9ffd9'));
insert into users (username,name,hashed_password) values ('hr','Кадровик','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');
insert into users (username,name,hashed_password) values ('worker','Работяга','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');
insert into users (username,name,hashed_password) values ('hr2','Кадровичка','$2a$10$PdgLWoVCsUK7Ju2txFxEYuRnS95G1pThYkZmOY3cYbP3fFvhvSNC6');
insert into users (username,name,hashed_password) values ('seller','Продаван','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe');
insert into users (username,name,hashed_password) values ('su','Вижу ВСЁ','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.');
insert into users (username,name,hashed_password) values ('analyst','Отчётник','$2a$10$2t3uKyI/UTEQ1eRiTKF0kO5Hz3sYoESzyLkZbKcov50y.sGIvmOCa');

insert into users_roles (users_user_id, roles) values ('1', 'USER');
insert into users_roles (users_user_id, roles) values ('2', 'USER');
insert into users_roles (users_user_id, roles) values ('2', 'ADMIN');
insert into users_roles (users_user_id, roles) values ('3', 'HR');
insert into users_roles (users_user_id, roles) values ('4', 'WORKS');
insert into users_roles (users_user_id, roles) values ('5', 'HR');
insert into users_roles (users_user_id, roles) values ('6', 'SALES');
insert into users_roles (users_user_id, roles) values ('7', 'GOD');
insert into users_roles (users_user_id, roles) values ('8', 'ANALYSTS');
//------------------------------------------------------------------------------------------------------------
//Статусы
INSERT INTO public.client_status(status) VALUES ('Обычный');
INSERT INTO public.client_status(status) VALUES ('Постоянный');
INSERT INTO public.client_status(status) VALUES ('Премиум');

INSERT INTO public.order_status(status) VALUES ('Создан');
INSERT INTO public.order_status(status) VALUES ('Распределен');
INSERT INTO public.order_status(status) VALUES ('Выполнен');
INSERT INTO public.order_status(status) VALUES ('Оплачен');
INSERT INTO public.order_status(status) VALUES ('Отменен');

INSERT INTO public.work_order_status(status) VALUES ('Назначен');
INSERT INTO public.work_order_status(status) VALUES ('В работе');
INSERT INTO public.work_order_status(status) VALUES ('Выполнен');
//------------------------------------------------------------------------------------------------------------
//Отчет по сотрудникам
DROP FUNCTION IF EXISTS public.get_employee_info();

CREATE OR REPLACE FUNCTION public.get_employee_info(
	)
    RETURNS TABLE(last_name character varying, first_name character varying, middle_name character varying, date_of_birth date, phone_number character varying, email character varying, comment character varying, age integer, "department" character varying, "position" character varying, salary numeric, workplace character varying, experience integer)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        e.last_name,
        e.first_name,
        e.middle_name,
        e.date_of_birth,
        e.phone_number::varchar(255),
        e.email::varchar(255),
        e.comment::varchar(255),
        EXTRACT(YEAR FROM age(e.date_of_birth))::integer AS age,
        st.department::varchar(255),
		st.position::varchar(255),
        st.salary,
        l.name::varchar(255) AS workplace,
        COALESCE((
            SELECT EXTRACT(YEAR FROM age(CURRENT_DATE, MIN(em_sub.date_of_employment)))::integer
            FROM employees_moving em_sub
            WHERE em_sub.employee_id = e.employee_id
        ), 0) AS experience
    FROM
        employees e
        RIGHT JOIN employees_moving em ON e.employee_id = em.employee_id
        LEFT JOIN staffing_table st ON em.staffing_table_id = st.staffing_table_id
        LEFT JOIN locations l ON st.location_id = l.location_id;
END;
$BODY$;

ALTER FUNCTION public.get_employee_info()
    OWNER TO postgres;
//------------------------------------------------------------------------------------------------------------
//Триггер и функция для создания бонусного счета.
-- FUNCTION: public.create_bonus_account_for_client()

-- DROP FUNCTION IF EXISTS public.create_bonus_account_for_client();

CREATE OR REPLACE FUNCTION public.create_bonus_account_for_client()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    -- Вставляем запись только если client_id отсутствует в bonus_account
    INSERT INTO public.bonus_account (client_id)
    SELECT NEW.client_id
    WHERE NOT EXISTS (
        SELECT 1
        FROM public.bonus_account
        WHERE client_id = NEW.client_id
    );

    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.create_bonus_account_for_client()
    OWNER TO postgres;

-- Trigger: trigger_create_bonus_account

-- DROP TRIGGER IF EXISTS trigger_create_bonus_account ON public.clients;

CREATE OR REPLACE TRIGGER trigger_create_bonus_account
    AFTER INSERT
    ON public.clients
    FOR EACH ROW
    EXECUTE FUNCTION public.create_bonus_account_for_client();
//------------------------------------------------------------------------------------------------------------
//Триггер и функция для создания даты открытия бонусного счета.
-- FUNCTION: public.set_open_date_of_bonus_account()

-- DROP FUNCTION IF EXISTS public.set_open_date_of_bonus_account();

CREATE OR REPLACE FUNCTION public.set_open_date_of_bonus_account()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 50
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    IF NEW.open_date IS NULL THEN
        NEW.open_date := CURRENT_DATE;
    END IF;
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.set_open_date_of_bonus_account()
    OWNER TO postgres;


-- Trigger: trigger_set_open_date_of_bonus_account
CREATE OR REPLACE TRIGGER set_open_date_of_bonus_account
    BEFORE INSERT
    ON public.bonus_account
    FOR EACH ROW
    EXECUTE FUNCTION public.set_open_date_of_bonus_account();
//------------------------------------------------------------------------------------------------------------
//Триггер, последовательность и функция для создания номера бонусного счета.
-- SEQUENCE: public.bonus_account_number_seq

-- DROP SEQUENCE IF EXISTS public.bonus_account_number_seq;

CREATE SEQUENCE IF NOT EXISTS public.bonus_account_number_seq
    INCREMENT 1
    START 1000000000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.bonus_account_number_seq
    OWNER TO postgres;

-- FUNCTION: public.set_bonus_account_number()

-- DROP FUNCTION IF EXISTS public.set_bonus_account_number();

CREATE OR REPLACE FUNCTION public.set_bonus_account_number()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    IF NEW.bonus_account_number IS NULL THEN
        NEW.bonus_account_number := nextval('public.bonus_account_number_seq');
    END IF;
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.set_bonus_account_number()
    OWNER TO postgres;

-- Trigger: trigger_set_bonus_account_number

-- DROP TRIGGER IF EXISTS trigger_set_bonus_account_number ON public.bonus_account;

CREATE OR REPLACE TRIGGER trigger_set_bonus_account_number
    BEFORE INSERT
    ON public.bonus_account
    FOR EACH ROW
    EXECUTE FUNCTION public.set_bonus_account_number();
//------------------------------------------------------------------------------------------------------------
//Отчет по офисам
CREATE OR REPLACE FUNCTION public.get_location_info()
    RETURNS TABLE (
    location_name VARCHAR(255),
    phone_number VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    building_number VARCHAR(255),
    postal_code VARCHAR(255),
    location_type_name VARCHAR(255)
)
    LANGUAGE 'plpgsql'
    COST 50
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        l.name::VARCHAR(255) AS location_name,
        l.phone_number::varchar(255),
        l.country,
        l.city,
        l.street,
        l.building_number,
        l.postal_code,
        lt.location_type_name
    FROM
        locations l
  LEFT JOIN locations_type lt ON l.location_id = lt.location_type_id;
END;
$BODY$;

ALTER FUNCTION public.set_open_date_of_bonus_account()
    OWNER TO postgres;
//------------------------------------------------------------------------------------------------------------
--Защита от дубликатов в расписание - уникальный индекс:
CREATE UNIQUE INDEX idx_schedule_unique_entry
ON public.schedule (work_day, employee_id, location_id, time_interval);


--Процедура для создания расписания для сотрудник в конкретном офисе на день(с проверкой существующих записей).
--CALL public.insert_schedule_entries('2025-04-08', 2, 1);
CREATE OR REPLACE PROCEDURE public.insert_schedule_entries(
    p_work_day date,
    p_employee_id bigint,
    p_location_id bigint
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM public.schedule
        WHERE work_day = p_work_day
          AND employee_id = p_employee_id
          AND location_id = p_location_id
        LIMIT 1
    ) THEN
        RAISE EXCEPTION 'DUPLICATE_SCHEDULE_ENTRY';
    END IF;

    INSERT INTO public.schedule (time_interval, work_day, employee_id, location_id)
    SELECT ti, p_work_day, p_employee_id, p_location_id
    FROM unnest(ARRAY[
        '09:00 - 10:00',
        '10:00 - 11:00',
        '11:00 - 12:00',
        '12:00 - 13:00',
        '14:00 - 15:00',
        '15:00 - 16:00',
        '16:00 - 17:00',
        '17:00 - 18:00'
    ]) AS ti;

EXCEPTION
    WHEN unique_violation THEN
        RAISE EXCEPTION 'DUPLICATE_SCHEDULE_ENTRY';
END;
$$;

ALTER PROCEDURE public.insert_schedule_entries(date, bigint, bigint)
    OWNER TO postgres;

//------------------------------------------------------------------------------------------------------------
-- Функция по получению расписания по всем сотрудникам в определенном офисе на дату
--SELECT * FROM public.get_schedule('2025-04-08', 1);
CREATE EXTENSION IF NOT EXISTS tablefunc;

CREATE OR REPLACE FUNCTION public.get_schedule(
    p_work_day date,
    p_location_id bigint
)
RETURNS TABLE (
    employee_name text,
    "09:00 - 10:00" int,
    "10:00 - 11:00" int,
    "11:00 - 12:00" int,
    "12:00 - 13:00" int,
    "14:00 - 15:00" int,
    "15:00 - 16:00" int,
    "16:00 - 17:00" int,
    "17:00 - 18:00" int,
    total int
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        e.last_name || ' ' || e.first_name::text AS employee_name,
        COALESCE(ct."09:00 - 10:00", 0),
        COALESCE(ct."10:00 - 11:00", 0),
        COALESCE(ct."11:00 - 12:00", 0),
        COALESCE(ct."12:00 - 13:00", 0),
        COALESCE(ct."14:00 - 15:00", 0),
        COALESCE(ct."15:00 - 16:00", 0),
        COALESCE(ct."16:00 - 17:00", 0),
        COALESCE(ct."17:00 - 18:00", 0),
        COALESCE(ct."09:00 - 10:00", 0) +
        COALESCE(ct."10:00 - 11:00", 0) +
        COALESCE(ct."11:00 - 12:00", 0) +
        COALESCE(ct."12:00 - 13:00", 0) +
        COALESCE(ct."14:00 - 15:00", 0) +
        COALESCE(ct."15:00 - 16:00", 0) +
        COALESCE(ct."16:00 - 17:00", 0) +
        COALESCE(ct."17:00 - 18:00", 0) AS total
    FROM crosstab(
        format(
            $QUERY$
            SELECT
                e.employee_id,
                s.time_interval,
                CASE WHEN s.work_orders_id IS NULL THEN 0 ELSE 1 END
            FROM public.schedule s
            JOIN public.employees e ON s.employee_id = e.employee_id
            WHERE
                s.work_day = %L
                AND s.location_id = %s
            ORDER BY e.employee_id, s.time_interval
            $QUERY$,
            p_work_day,
            p_location_id
        ),
        $CATEGORIES$
        VALUES
            ('09:00 - 10:00'),
            ('10:00 - 11:00'),
            ('11:00 - 12:00'),
            ('12:00 - 13:00'),
            ('14:00 - 15:00'),
            ('15:00 - 16:00'),
            ('16:00 - 17:00'),
            ('17:00 - 18:00')
        $CATEGORIES$
    ) AS ct(
        employee_id bigint,
        "09:00 - 10:00" int,
        "10:00 - 11:00" int,
        "11:00 - 12:00" int,
        "12:00 - 13:00" int,
        "14:00 - 15:00" int,
        "15:00 - 16:00" int,
        "16:00 - 17:00" int,
        "17:00 - 18:00" int
    )
    JOIN public.employees e ON ct.employee_id = e.employee_id
    ORDER BY e.last_name, e.first_name;
END;
$$;
//------------------------------------------------------------------------------------------------------------
--Процедура удаления графика по сотруднику (на определенную дату, в определенном офисе)
CREATE OR REPLACE PROCEDURE public.delete_schedule_entries(
    p_work_day date,
    p_employee_id bigint,
    p_location_id bigint
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_has_orders boolean;
    v_has_records boolean;
BEGIN
    -- Проверка на наличие связанных заказов
    SELECT EXISTS (
        SELECT 1
        FROM public.schedule
        WHERE
            work_day = p_work_day
            AND employee_id = p_employee_id
            AND location_id = p_location_id
            AND work_orders_id IS NOT NULL
    ) INTO v_has_orders;

    IF v_has_orders THEN
        RAISE EXCEPTION 'Невозможно удалить расписание. Существуют связанные заказы (work_orders_id).';
    END IF;

    -- Проверка на существование записей
    SELECT EXISTS (
        SELECT 1
        FROM public.schedule
        WHERE
            work_day = p_work_day
            AND employee_id = p_employee_id
            AND location_id = p_location_id
    ) INTO v_has_records;

    IF NOT v_has_records THEN
        RAISE EXCEPTION 'Записи для удаления не найдены (день: %, сотрудник: %, локация: %).',
            p_work_day,
            p_employee_id,
            p_location_id;
    END IF;

    -- Удаление записей
    DELETE FROM public.schedule
    WHERE
        work_day = p_work_day
        AND employee_id = p_employee_id
        AND location_id = p_location_id;

    RAISE NOTICE 'Удалено расписание для сотрудника % в локации % на день %.',
        p_employee_id,
        p_location_id,
        p_work_day;
END;
$$;
//------------------------------------------------------------------------------------------------------------
// Orders. Создать триггер и функцию по первичному присвоения Даты, номера, Статуса при создании.
CREATE SEQUENCE IF NOT EXISTS public.order_number_seq
    INCREMENT 1
    START 1000000000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.order_number_seq
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION set_order_default_info()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.date_of_order := CURRENT_DATE;
    NEW.order_status_id := 1;
    IF NEW.number_of_order IS NULL THEN
        NEW.number_of_order := nextval('public.order_number_seq');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER order_created
    BEFORE INSERT ON orders
    FOR EACH ROW
EXECUTE FUNCTION set_order_default_info();
------------------------------------------------------------------------------------------------------------
// WorkOrders последовательность, функция и триггер для добавления даты, номера и статуса при создании
CREATE SEQUENCE IF NOT EXISTS public.work_order_number_seq
    INCREMENT 1
    START 1000000000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.work_order_number_seq
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION set_work_order_default_info()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.date_of_work_order := CURRENT_DATE;
    NEW.work_order_status_id := 1;
    IF NEW.number_of_work_order IS NULL THEN
        NEW.number_of_work_order := nextval('public.work_order_number_seq');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER work_order_created
    BEFORE INSERT ON work_orders
    FOR EACH ROW
EXECUTE FUNCTION set_work_order_default_info();
------------------------------------------------------------------------------------------------------------