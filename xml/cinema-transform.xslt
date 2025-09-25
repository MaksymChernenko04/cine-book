<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cd="http://maksymchernenko.com/cine-book">

    <xsl:template match="/">
        <html>
            <head>
                <title>Cinema Data</title>
                <style>
                    h2 {
                        font-size: 25px;
                    }

                    th, td {
                        text-align: center;
                        padding: 5px;
                    }

                    .red-status {
                        background-color: red;
                        padding: 2px;
                        color: white;
                        border-radius: 5px;
                    }

                    .yellow-status {
                        background-color: yellow;
                        padding: 2px;
                        color: black;
                        border-radius: 5px;
                    }

                    .green-status {
                        background-color: green;
                        padding: 2px;
                        color: white;
                        border-radius: 5px;
                    }
                </style>

                <script>
                    <![CDATA[
                        function filterMovies() {
                            const input = document.getElementById('movieFilter');
                            const filter = input.value.toLowerCase();
                            const table = document.querySelector('table');
                            const tr = table.getElementsByTagName('tr');

                            for (let i = 1; i < tr.length; i++) {
                                const td = tr[i].getElementsByTagName('td')[0];
                                if (td) {
                                    const txtValue = td.textContent || td.innerText;
                                    tr[i].style.display = txtValue.toLowerCase().includes(filter) ? '' : 'none';
                                }
                            }
                        }
                    ]]>
                </script>
            </head>
            <body>
                <h1>Cinema Data</h1>

                <h2>Movies</h2>

                <input type="text" id="movieFilter" placeholder="Filter movie by title" onkeyup="filterMovies()" style="margin-bottom: 10px; padding: 5px; width: 300px;"/>

                <table border="1px solid black" style="border-collapse: collapse">
                    <tr bgcolor="#cccccc">
                        <th>Movie</th>
                        <th>Description</th>
                        <th>Genre</th>
                        <th>Duration (min)</th>
                        <th>Rating</th>
                        <th>Release Date</th>
                    </tr>
                    <xsl:for-each select="/*/cd:Movies/cd:Movie">
                        <tr>
                            <td style="text-align: center">
                                <img style="max-height: 100px" src="{cd:PosterURL}"/>
                                <br/>
                                <xsl:value-of select="cd:Title"/>
                            </td>
                            <td><p><xsl:value-of select="cd:Description"/></p></td>
                            <td><xsl:value-of select="cd:Genre"/></td>
                            <td><xsl:value-of select="cd:DurationMinutes"/></td>
                            <td><xsl:value-of select="cd:Rating"/></td>
                            <td><xsl:value-of select="cd:ReleaseDate"/></td>
                        </tr>
                    </xsl:for-each>
                </table>

                <h2>Screenings</h2>
                <table border="1px solid black" style="border-collapse: collapse">
                    <tr bgcolor="#cccccc">
                        <th>Movie</th>
                        <th>Start Time</th>
                        <th>Hall</th>
                        <th>Seats</th>
                    </tr>
                    <xsl:for-each select="/*/cd:Screenings/cd:Screening">
                        <tr>
                            <td style="text-align: center">
                                <img style="max-height: 100px" src="{cd:Movie/cd:PosterURL}"/>
                                <br/>
                                <xsl:value-of select="cd:Movie/cd:Title"/>
                            </td>
                            <td>
                                <xsl:value-of select="substring(cd:StartTime, 12, 5)"/>
                                <br/>
                                <xsl:value-of select="substring(cd:StartTime, 1, 10)"/>
                            </td>
                            <td>
                                <xsl:value-of select="cd:Hall/cd:Name"/>
                                <br/>
                                <xsl:if test="cd:Hall/@accessibility = 'true'"><span class="green-status">ACCESSIBLE</span></xsl:if>
                            </td>
                            <td style="text-align: left;">
                                <ul style="padding-left: 20px;">
                                    <xsl:for-each select="cd:Hall/cd:Seats/cd:Seat">
                                        <li>
                                            <xsl:value-of select="concat('Row ', cd:Row, ', №', cd:Number)"/>
                                            <xsl:choose>
                                                <xsl:when test="cd:Type = 'VIP'"><b><xsl:value-of select="concat(' (', cd:Type, ') ')"/></b></xsl:when>
                                                <xsl:otherwise><xsl:value-of select="concat(' (', cd:Type, ') ')"/></xsl:otherwise>
                                            </xsl:choose>
                                            <xsl:value-of select="concat('Price - ', cd:Price, cd:Price/@currency, ' ')"/>
                                            <xsl:if test="cd:Status = 'SOLD'"><span class="red-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                            <xsl:if test="cd:Status = 'LOCKED'"><span class="yellow-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                            <xsl:if test="cd:Status = 'AVAILABLE'"><span class="green-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                            <xsl:if test="position() != last()"><br/><br/></xsl:if>
                                        </li>
                                    </xsl:for-each>
                                </ul>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>

                <h2>Bookings</h2>
                <table border="1px solid black" style="border-collapse: collapse">
                    <tr bgcolor="#cccccc">
                        <th>User</th>
                        <th>Status</th>
                        <th>Screening</th>
                        <th>Booked Seats</th>
                        <th>Total Price</th>
                        <th>Payment</th>
                    </tr>
                    <xsl:for-each select="/*/cd:Bookings/cd:Booking">
                        <tr>
                            <td style="text-align: left;">
                                <xsl:value-of select="concat('Name: ', cd:User/cd:Name)"/>
                                <br/>
                                <xsl:value-of select="concat('Email: ', cd:User/cd:Email)"/>
                            </td>
                            <td>
                                <xsl:if test="cd:BookingStatus = 'CANCELED'"><span class="red-status"><xsl:value-of select="cd:BookingStatus"/></span></xsl:if>
                                <xsl:if test="cd:BookingStatus = 'PENDING'"><span class="yellow-status"><xsl:value-of select="cd:BookingStatus"/></span></xsl:if>
                                <xsl:if test="cd:BookingStatus = 'CONFIRMED'"><span class="green-status"><xsl:value-of select="cd:BookingStatus"/></span></xsl:if>
                            </td>
                            <td style="text-align: left;">
                                <xsl:value-of select="concat('Movie: ', cd:Screening/cd:Movie/cd:Title)"/>
                                <br/>
                                <xsl:value-of select="concat('Start Time: ', substring(cd:Screening/cd:StartTime, 12, 5))"/>
                                <br/>
                                <xsl:value-of select="concat('Date: ', substring(cd:Screening/cd:StartTime, 1, 10))"/>
                                <br/>
                                <xsl:value-of select="concat('Hall: ', cd:Screening/cd:Hall/cd:Name)"/>
                            </td>
                            <td>
                                <xsl:for-each select="cd:BookedSeats/cd:Seat">
                                    <xsl:value-of select="concat('Row ', cd:Row, ', №', cd:Number, ' (', cd:Type, ') ', 'Price - ', cd:Price, cd:Price/@currency, ' ')"/>
                                    <xsl:if test="cd:Status = 'SOLD'"><span class="red-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                    <xsl:if test="cd:Status = 'LOCKED'"><span class="yellow-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                    <xsl:if test="cd:Status = 'AVAILABLE'"><span class="green-status"><xsl:value-of select="cd:Status"/></span></xsl:if>
                                    <xsl:if test="position() != last()"><br/><br/></xsl:if>
                                </xsl:for-each>
                            </td>
                            <td><xsl:value-of select="concat(cd:TotalPrice, cd:TotalPrice/@currency)"/></td>
                            <td>
                                <xsl:value-of select="concat('Method: ', cd:Payment/cd:Method)"/>
                                <br/>
                                <xsl:choose>
                                    <xsl:when test="cd:Payment/cd:PaidAt"> paid: <xsl:value-of select="cd:Payment/cd:PaidAt"/></xsl:when>
                                    <xsl:when test="cd:Payment/cd:ExpectedRedirectURL"> redirection:
                                        <a href="{cd:Payment/cd:ExpectedRedirectURL}" target="_blank">
                                            <xsl:value-of select="cd:Payment/cd:ExpectedRedirectURL"/>
                                        </a>
                                    </xsl:when>
                                </xsl:choose>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>