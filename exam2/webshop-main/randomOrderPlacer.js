const customers = require("./data/customers.js")
const prints = require("./data/prints.js")

const http = require('http');

const options = {
    hostname: 'localhost',
    port: 3000,
    path: '/order',
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    }
};

let postJSON = (options, postData, callback) => {
    let req = http.request(options, (res) => {
        let output = ''
        res.setEncoding('utf8')

        // Listener to receive data
        res.on('data', (chunk) => {
            output += chunk
        });

        res.on('end', () => {
            let obj = JSON.parse(output)
            callback(res.statusCode, obj)
        });
    });

    req.on('error', (err) => {
        //res.send('error: ' + err.message)
    });

    req.write(postData)
    // Ending the request
    req.end()
};

function generateOrderJSON() {
    const chosenPrint = prints[Math.floor(Math.random()*prints.length)];
    const materials = ["PLA", "ABS", "PETG"]
    const shippingOptions = ["Pickup Amsterdam", "Pickup Enschede", "Mail"]
    const colors = ["Red", "Blue", "Green", "Black", "Pink", "Yellow"]
    let chosenColors = []
    for(i = 0; i < chosenPrint.nrColors; i++) {
        chosenColors.push(colors[Math.floor(Math.random()*colors.length)])
    }
    return JSON.stringify({
        customer: customers[Math.floor(Math.random()*customers.length)].id,
        print: chosenPrint.name,
        material: materials[Math.floor(Math.random()*materials.length)],
        shipping: shippingOptions[Math.floor(Math.random()*shippingOptions.length)],
        colors: chosenColors
    })
}

async function interval() {
    let d = Math.random();

    //10% chance of a new order being placed.
    if(d < 0.1) {
        postJSON(options, generateOrderJSON(), (statusCode, result) => {
            console.log("Order placed")
        });
    }
}

setInterval(interval, 1000);