const HttpError = require("../errors/HttpError");

module.exports = {
    checkKeys: function(body, allowedKeys) {
        for(let key of allowedKeys) {
            if(!body.hasOwnProperty(key)) {
                throw new HttpError("Body does not contain " + key + " key", 400);
            }
        }

        for(let key in body) {
            if(!allowedKeys.includes(key)) {
                throw new HttpError("The allowed keys does not include the given: " + key + " key", 400);
            }
        }
    }
}
