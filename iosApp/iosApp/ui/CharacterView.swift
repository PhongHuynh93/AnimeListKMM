//
//  CharacterView.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared
import KingfisherSwiftUI

struct CharacterView: View {
    let item: Character
    var body: some View {
        KFImage(URL(string: item.imageUrl))
            .placeholder {
                VStack {
                    Color.gray
                }
            }
            .resizable()
            .aspectRatio(1, contentMode:.fit)
            .clipShape(Circle())
    }
}
