# Openfire
- [Download .tar.gz](https://github.com/igniterealtime/Openfire/releases/tag/v4.0.3) and extract
- start using command `{openfire_directory}/bin/openfire start`
- stop using command `{openfire_directory}/bin/openfire stop`

## Server setup
- goto `http://localhost:9090/`
- select language
- Server Settings
  - Domain: localhost
  - Admin Console Port: 9090 (default)
  - Secure Admin Console Port: 9091 (default)
  - Property Encryption via: Blowfish (default)
- Database Settings: Embedded Database
- Profile Settings: Default
- Administrator Account
  - Set username password and note it.

## Add users 
- Login with admin creds
- on the Users/Groups tab, create the 3 users specified in the book with following username/password
  1. sniper/sniper
  2. auction-item-54321/auction
  3. auction-item-65432/auction

## Additional Server Settings
- under Server -> Server Settings -> Resource Policy, set Conflict Policy to Never kick and Save.
- under Server -> Server Settings -> Offline Messages, select Drop and Save.