## 🔌 gRPC 接口草图（`proto/crypto.proto`）

```proto
syntax = "proto3";

package crypto;

// 用户密钥请求
message KeyGenRequest {
  string user_id = 1;
}

// 密钥对返回
message KeyGenResponse {
  string public_key = 1;
  string private_key = 2;
}

// 身份认证挑战请求
message AuthChallengeRequest {
  string user_id = 1;
  string nonce = 2;
  string public_key = 3;
}

// 身份认证响应结果
message AuthChallengeResponse {
  string signature = 1;
  string proof = 2;
}

// 加密服务接口定义
service CryptoService {
  rpc GenerateKeyPair (KeyGenRequest) returns (KeyGenResponse);
  rpc GenerateAuthProof (AuthChallengeRequest) returns (AuthChallengeResponse);
}
```
