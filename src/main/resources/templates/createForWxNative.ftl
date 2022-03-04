<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>支付</title>
</head>
<body>
    <div id="myQrcode" style="text-align: center; margin-top: 100px"></div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
    <script>
        jQuery('#myQrcode').qrcode({
            text: "${codeUrl}"
        });

        $(function () {
            let timerID = setInterval(function () {
                $.ajax({
                    url: "/pay/queryByOrderId",
                    data: {
                        orderId: "${orderId}"
                    },
                    success: function (data) {
                        if (data === "SUCCESS") {
                            location.href = "${returnUrl}";
                        }
                    }
                });
            }, 1000);

            // 两小时后支付码失效
            setTimeout(function () {
                clearInterval(timerID);
                alert("支付超时，请重新发起支付");
                location.href = ""
            }, 1000 * 60 * 60 * 2);
        })
    </script>
</body>
</html>