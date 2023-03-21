# JWT

1. Security 영역과 JWT 영역을 분리해서 생각
   - Security는 필터, 인증 객체 생성, Security Context에 인증 객체를 저장하는 로직을 제공
   - JWT는 토큰 인코딩 관련 라이브러리이며, 토큰의 생성 또는 토큰 검증을 할 수 있는 로직을 직접 구현해야 함

2. 진행 순서
   - Filter가 Request(요청)을 낚아챔
   - UsernamePasswordToken과 Authentication 객체를 만듦
   - AuthenticationManager가 AuthenticationProvider에게 위임을 한다. 
   - Authentication Provider는 직접 DB를 거쳐서 Username & Password를 검증하고, Principal객체를 반환한다.
   - Filter까지 계속해서 반환하고, Filter는 Security Context Holder에 Authentication 객체를 저장하고 DispatcherServlet으로 요청을 허가



